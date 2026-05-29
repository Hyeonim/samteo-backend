package com.samteo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samteo.dto.response.FestivalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.net.URI;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FestivalService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${external.tourapi.service-key}")
    private String serviceKey;

    @Value("${external.tourapi.base-url}")
    private String baseUrl;

    public FestivalService(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public List<FestivalResponse> getFestivals(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        String startDate = String.format("%d%02d01", year, month);
        String endDate = String.format("%d%02d%02d", year, month, yearMonth.lengthOfMonth());

        URI uri = URI.create(baseUrl + "/searchFestival2"
                + "?serviceKey=" + serviceKey
                + "&MobileOS=ETC"
                + "&MobileApp=samteo"
                + "&_type=json"
                + "&eventStartDate=" + startDate
                + "&eventEndDate=" + endDate
                + "&arrange=A"
                + "&numOfRows=100"
                + "&pageNo=1");

        try {
            String body = restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(String.class);

            JsonNode items = objectMapper.readTree(body)
                    .path("response").path("body").path("items").path("item");

            return parseItems(items);
        } catch (Exception e) {
            log.error("TourAPI 호출 실패: {}", e.getMessage());
            throw new RuntimeException("축제 정보를 불러오는 데 실패했습니다.");
        }
    }

    private List<FestivalResponse> parseItems(JsonNode items) {
        List<FestivalResponse> result = new ArrayList<>();
        if (items.isArray()) {
            items.forEach(item -> result.add(mapToFestivalResponse(item)));
        } else if (!items.isMissingNode() && !items.isNull()) {
            result.add(mapToFestivalResponse(items));
        }
        return result;
    }

    private FestivalResponse mapToFestivalResponse(JsonNode item) {
        return FestivalResponse.builder()
                .title(textOf(item, "title"))
                .startDate(formatDate(textOf(item, "eventstartdate")))
                .endDate(formatDate(textOf(item, "eventenddate")))
                .location(extractRegion(textOf(item, "addr1")))
                .build();
    }

    private String textOf(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return (value != null && !value.isNull()) ? value.asText() : null;
    }

    private String formatDate(String raw) {
        if (raw == null || raw.length() != 8) return raw;
        return raw.substring(0, 4) + "-" + raw.substring(4, 6) + "-" + raw.substring(6, 8);
    }

    private String extractRegion(String addr) {
        if (addr == null || addr.isBlank()) return null;
        String first = addr.trim().split("\\s+")[0];
        for (String suffix : List.of("특별자치도", "특별자치시", "특별시", "광역시", "도")) {
            if (first.endsWith(suffix)) {
                return first.substring(0, first.length() - suffix.length());
            }
        }
        return first;
    }
}
