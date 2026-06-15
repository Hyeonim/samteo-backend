package com.samteo.domain.festival.service;

import com.samteo.domain.festival.dto.response.FestivalResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 적재된 축제 데이터를 로컬 DB에서 조회한다.
 */
@Service
public class FestivalService {

    private static final Duration CACHE_TTL = Duration.ofHours(1);

    private final JdbcTemplate jdbcTemplate;
    private final Map<YearMonth, CacheEntry> cache = new ConcurrentHashMap<>();

    public FestivalService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 지정한 연월의 축제를 적재 테이블에서 조회한다.
     *
     * @param year 조회 대상 연도
     * @param month 조회 대상 월
     * @return 축제 목록
     */
    public List<FestivalResponse> getFestivals(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        CacheEntry cached = cache.get(yearMonth);
        if (cached != null && !cached.isExpired()) {
            return cached.festivals();
        }

        List<FestivalResponse> festivals = List.copyOf(getLocalFestivals(yearMonth));
        cache.put(yearMonth, new CacheEntry(festivals, System.currentTimeMillis() + CACHE_TTL.toMillis()));
        return festivals;
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

    private record CacheEntry(List<FestivalResponse> festivals, long expiresAtMillis) {

        private boolean isExpired() {
            return System.currentTimeMillis() >= expiresAtMillis;
        }
    }
}
