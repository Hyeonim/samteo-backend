package com.samteo.domain.job.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samteo.domain.job.dto.response.JobResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 관광 일자리 API와 연동하여 원본 응답을 서비스용 DTO로 정규화한다.
 */
@Slf4j
@Service
public class JobService {

    private static final Duration CACHE_TTL = Duration.ofMinutes(10);
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(3);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(5);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final Map<Integer, CacheEntry> cache = new ConcurrentHashMap<>();

    @Value("${external.tourapi.api-key}")
    private String apiKey;

    @Value("${external.gwanwangin.base-url}")
    private String baseUrl;

    public JobService(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
        this.restClient = restClientBuilder
                .requestFactory(requestFactory())
                .build();
        this.objectMapper = objectMapper;
    }

    /**
     * 외부 API에서 채용 공고를 조회하고 응답 객체 목록으로 변환한다.
     *
     * @param size 외부 API에 요청할 공고 개수
     * @return 정규화된 채용 공고 목록
     */
    public List<JobResponse> getJobs(int size) {
        CacheEntry cached = cache.get(size);
        if (cached != null && !cached.isExpired()) {
            return cached.getJobs();
        }

        URI uri = UriComponentsBuilder.fromUriString(baseUrl + "/empmnInfoList")
                .queryParam("serviceKey", URLEncoder.encode(apiKey, StandardCharsets.UTF_8))
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "samteo")
                .queryParam("numOfRows", size)
                .queryParam("pageNo", 1)
                .queryParam("_type", "json")
                .build(true)
                .toUri();

        try {
            String body = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(String.class);

            JsonNode items = objectMapper.readTree(body)
                    .path("response").path("body").path("items").path("item");

            List<JobResponse> jobs = List.copyOf(parseItems(items));
            cache.put(size, new CacheEntry(jobs, System.currentTimeMillis() + CACHE_TTL.toMillis()));
            return jobs;
        } catch (Exception e) {
            log.error("Gwanwangin API call failed: {}", e.getMessage());
            if (cached != null) {
                return cached.getJobs();
            }
            throw new RuntimeException("Failed to load jobs.");
        }
    }

    private SimpleClientHttpRequestFactory requestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(CONNECT_TIMEOUT);
        factory.setReadTimeout(READ_TIMEOUT);
        return factory;
    }

    private List<JobResponse> parseItems(JsonNode items) {
        List<JobResponse> result = new ArrayList<>();
        if (items.isArray()) {
            items.forEach(item -> result.add(mapToJobResponse(item)));
        } else if (!items.isMissingNode() && !items.isNull()) {
            result.add(mapToJobResponse(items));
        }
        return result;
    }

    private JobResponse mapToJobResponse(JsonNode item) {
        return JobResponse.builder()
                .id(textOf(item, "empmnInfoNo"))
                .type(textOf(item, "uprRcritJssfcCd"))
                .title(textOf(item, "empmnTtl"))
                .company(textOf(item, "corpoNm"))
                .location(textOf(item, "wrkpAdres"))
                .wage(intOf(item, "wageAmt"))
                .startDate(extractDate(textOf(item, "regDt")))
                .endDate(extractDate(textOf(item, "rcptDdlnDe")))
                .build();
    }

    private String textOf(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return (value != null && !value.isNull()) ? value.asText() : null;
    }

    private Integer intOf(JsonNode node, String field) {
        JsonNode value = node.get(field);
        if (value == null || value.isNull()) {
            return null;
        }
        String digits = value.asText().replaceAll("[^0-9]", "");
        return digits.isEmpty() ? null : Integer.parseInt(digits);
    }

    private String extractDate(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        return raw.length() >= 10 ? raw.substring(0, 10) : raw;
    }

    private static class CacheEntry {

        private final List<JobResponse> jobs;
        private final long expiresAtMillis;

        private CacheEntry(List<JobResponse> jobs, long expiresAtMillis) {
            this.jobs = jobs;
            this.expiresAtMillis = expiresAtMillis;
        }

        private List<JobResponse> getJobs() {
            return jobs;
        }

        private boolean isExpired() {
            return System.currentTimeMillis() >= expiresAtMillis;
        }
    }
}
