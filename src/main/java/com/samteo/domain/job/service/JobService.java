package com.samteo.domain.job.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samteo.domain.job.dto.response.JobResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** ALIO 공공기관 채용 공고를 서비스 응답 형식으로 변환한다. */
@Slf4j
@Service
public class JobService {

    private static final int MONTHLY_WORK_HOURS = 209;
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(15);

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final Map<String, JobPage> cache = new ConcurrentHashMap<>();

    @Value("${external.alio.api-key:}")
    private String apiKey;

    @Value("${external.alio.base-url}")
    private String baseUrl;

    @Value("${jobs.default-hourly-wage:10320}")
    private int defaultHourlyWage;

    public JobService(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
        this.restClient = restClientBuilder.requestFactory(requestFactory()).build();
        this.objectMapper = objectMapper;
    }

    public List<JobResponse> getJobs(int requestedSize) {
        return getJobs(requestedSize, null);
    }

    public List<JobResponse> getJobs(int requestedSize, String workRegionCode) {
        return getJobsPage(1, requestedSize, workRegionCode).jobs();
    }

    public JobPage getJobsPage(int requestedPage, int requestedSize, String workRegionCode) {
        int page = Math.max(1, requestedPage);
        int size = normalizeSize(requestedSize);
        String cacheKey = page + ":" + size + ":" + (workRegionCode == null ? "ALL" : workRegionCode);
        JobPage cached = cache.get(cacheKey);
        if (cached != null && !cached.isExpired()) return cached;

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("pageNo", String.valueOf(page));
        form.add("pageSet", String.valueOf(size));
        form.add("numOfRows", String.valueOf(size));
        form.add("ongoingYn", "Y");
        if (workRegionCode != null && !workRegionCode.isBlank()) {
            form.add("workRgnLst", workRegionCode);
        }
        if (apiKey != null && !apiKey.isBlank()) form.add("serviceKey", apiKey);

        try {
            String body = restClient.post()
                    .uri(baseUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(String.class);
            JsonNode data = objectMapper.readTree(body).path("data");
            JsonNode items = data.path("result");
            List<JobResponse> jobs = List.copyOf(parseItems(items).stream().limit(size).toList());
            long totalCount = data.path("totalCount").asLong(jobs.size());
            JobPage result = new JobPage(jobs, page, size, totalCount,
                    System.currentTimeMillis() + CACHE_TTL.toMillis());
            cache.put(cacheKey, result);
            return result;
        } catch (Exception e) {
            log.error("ALIO recruitment API call failed: {}", e.getMessage());
            if (cached != null) return cached;
            throw new RuntimeException("ALIO 채용 정보를 불러오지 못했습니다.", e);
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
        if (items.isArray()) items.forEach(item -> result.add(mapToJobResponse(item)));
        return result;
    }

    private JobResponse mapToJobResponse(JsonNode item) {
        String category = firstText(item, "ncsCdNmLst", "recrutSeNm");
        String qualification = textOf(item, "aplyQlfcCn");
        return JobResponse.builder()
                .id(textOf(item, "recrutPblntSn"))
                .type(category)
                .title(textOf(item, "recrutPbancTtl"))
                .company(textOf(item, "instNm"))
                .location(textOf(item, "workRgnNmLst"))
                .wage(defaultHourlyWage)
                .hourlyWage(defaultHourlyWage)
                .monthlySalary(defaultHourlyWage * MONTHLY_WORK_HOURS)
                .employmentType(textOf(item, "hireTypeNmLst"))
                .education(textOf(item, "acbgCondNmLst"))
                .description(qualification)
                .startDate(formatDate(textOf(item, "pbancBgngYmd")))
                .endDate(formatDate(textOf(item, "pbancEndYmd")))
                .sourceUrl(textOf(item, "srcUrl"))
                .build();
    }

    private int normalizeSize(int size) {
        if (size <= 10) return 10;
        if (size <= 20) return 20;
        if (size <= 50) return 50;
        return 100;
    }

    private String firstText(JsonNode node, String... fields) {
        for (String field : fields) {
            String value = textOf(node, field);
            if (value != null && !value.isBlank()) return value;
        }
        return null;
    }

    private String textOf(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asText();
    }

    private String formatDate(String raw) {
        if (raw == null || raw.length() != 8) return raw;
        return raw.substring(0, 4) + "-" + raw.substring(4, 6) + "-" + raw.substring(6, 8);
    }

    public record JobPage(List<JobResponse> jobs, int page, int size, long totalCount, long expiresAtMillis) {
        private boolean isExpired() { return System.currentTimeMillis() >= expiresAtMillis; }
    }
}
