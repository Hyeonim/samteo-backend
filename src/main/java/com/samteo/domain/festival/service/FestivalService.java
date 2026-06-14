package com.samteo.domain.festival.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samteo.domain.festival.dto.response.FestivalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TourAPI 축제 데이터와 로컬 더미 축제 데이터를 함께 조회한다.
 */
@Slf4j
@Service
public class FestivalService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;

    @Value("${external.tourapi.service-key}")
    private String serviceKey;

    @Value("${external.tourapi.base-url}")
    private String baseUrl;

    public FestivalService(RestClient.Builder restClientBuilder, ObjectMapper objectMapper, JdbcTemplate jdbcTemplate) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 지정한 연월의 축제를 조회한다. 로컬 DB 데이터는 항상 포함하고, TourAPI 장애 시에는 로컬 데이터로 대체한다.
     *
     * @param year 조회 대상 연도
     * @param month 조회 대상 월
     * @return 축제 목록
     */
    public List<FestivalResponse> getFestivals(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        String startDate = String.format("%d%02d01", year, month);
        String endDate = String.format("%d%02d%02d", year, month, yearMonth.lengthOfMonth());
        List<FestivalResponse> localFestivals = getLocalFestivals(yearMonth);

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

            return mergeFestivals(localFestivals, parseItems(items));
        } catch (Exception e) {
            log.error("TourAPI call failed: {}", e.getMessage());
            if (!localFestivals.isEmpty()) {
                return localFestivals;
            }
            throw new RuntimeException("Failed to load festivals.");
        }
    }

    private List<FestivalResponse> getLocalFestivals(YearMonth yearMonth) {
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        String sql = """
                SELECT
                  tc.title,
                  df.event_start_date,
                  df.event_end_date,
                  df.area_code,
                  tc.addr1
                FROM DETAIL_FESTIVAL df
                JOIN TOUR_CONTENT tc ON tc.content_id = df.content_id
                JOIN META_AREA ma ON ma.area_code = df.area_code
                WHERE df.event_start_date <= ?
                  AND df.event_end_date >= ?
                ORDER BY df.event_start_date, tc.title
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> FestivalResponse.builder()
                .title(rs.getString("title"))
                .startDate(formatDate(rs.getDate("event_start_date")))
                .endDate(formatDate(rs.getDate("event_end_date")))
                .location(resolveLocalRegion(rs.getInt("area_code"), rs.getString("addr1")))
                .build(), Date.valueOf(lastDay), Date.valueOf(firstDay));
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

    private List<FestivalResponse> mergeFestivals(List<FestivalResponse> local, List<FestivalResponse> external) {
        Map<String, FestivalResponse> merged = new LinkedHashMap<>();
        for (FestivalResponse festival : local) {
            merged.put(festivalKey(festival), festival);
        }
        for (FestivalResponse festival : external) {
            merged.putIfAbsent(festivalKey(festival), festival);
        }
        return new ArrayList<>(merged.values());
    }

    private String festivalKey(FestivalResponse festival) {
        return festival.getTitle() + "|" + festival.getStartDate() + "|" + festival.getEndDate();
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
        if (raw == null || raw.length() != 8) {
            return raw;
        }
        return raw.substring(0, 4) + "-" + raw.substring(4, 6) + "-" + raw.substring(6, 8);
    }

    private String formatDate(Date date) {
        return date == null ? null : date.toLocalDate().toString();
    }

    private String extractRegion(String addr) {
        if (addr == null || addr.isBlank()) {
            return null;
        }
        String[] tokens = addr.trim().split("\\s+");
        String first = tokens[0];
        if (first.matches("\\d+.*") && tokens.length > 1) {
            first = tokens[1];
        }
        if ((first.endsWith("도") || first.endsWith("특별자치도")) && tokens.length > 1) {
            return normalizeRegionToken(tokens[1]);
        }
        return normalizeRegionToken(first);
    }

    private String resolveLocalRegion(int areaCode, String addr) {
        return switch (areaCode) {
            case 1 -> "서울";
            case 2 -> "인천";
            case 3 -> "대전";
            case 4 -> "대구";
            case 5 -> "광주";
            case 6 -> "부산";
            case 7 -> "울산";
            case 8 -> "세종";
            case 31 -> "경기";
            case 32 -> extractRegion(addr);
            case 33 -> "충북";
            case 34 -> "충남";
            case 35 -> extractRegion(addr);
            case 36 -> "경남";
            case 37 -> "전주";
            case 38 -> extractRegion(addr);
            case 39 -> "제주";
            default -> extractRegion(addr);
        };
    }

    private String normalizeRegionToken(String token) {
        for (String suffix : List.of("특별자치도", "특별자치시", "특별시", "광역시", "도", "시", "군", "구")) {
            if (token.endsWith(suffix)) {
                return token.substring(0, token.length() - suffix.length());
            }
        }
        return token;
    }
}
