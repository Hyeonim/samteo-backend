package com.samteo.domain.job.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samteo.domain.job.dto.response.JobResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * 관광 일자리 API와 연동하여 원본 응답을 서비스용 DTO로 정규화한다.
 */
@Slf4j
@Service
public class JobService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${external.gwanwangin.api-key}")
    private String apiKey;

    @Value("${external.gwanwangin.base-url}")
    private String baseUrl;

    public JobService(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    /**
     * 외부 API에서 채용 공고를 조회하고 응답 객체 목록으로 변환한다.
     *
     * @param size 외부 API에 요청할 공고 개수
     * @return 정규화된 채용 공고 목록
     */
    public List<JobResponse> getJobs(int size) {
        URI uri = URI.create(baseUrl + "/empmnInfoList"
                + "?serviceKey=" + apiKey
                + "&MobileOS=ETC"
                + "&MobileApp=samteo"
                + "&numOfRows=" + size
                + "&pageNo=1"
                + "&_type=json");

        try {
            String body = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(String.class);

            JsonNode items = objectMapper.readTree(body)
                    .path("response").path("body").path("items").path("item");

            return parseItems(items);
        } catch (Exception e) {
            log.error("Gwanwangin API call failed: {}", e.getMessage());
            throw new RuntimeException("Failed to load jobs.");
        }
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
}
