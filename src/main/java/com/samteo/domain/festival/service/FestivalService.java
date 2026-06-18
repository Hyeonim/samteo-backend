package com.samteo.domain.festival.service;

import com.samteo.domain.festival.dto.response.FestivalResponse;
import com.samteo.domain.tourapi.dto.response.TourContentResponse;
import com.samteo.domain.tourapi.service.TourApiService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FestivalService {

    private static final Duration CACHE_TTL = Duration.ofHours(1);
    private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final TourApiService tourApiService;
    private final Map<YearMonth, CacheEntry> cache = new ConcurrentHashMap<>();

    public FestivalService(TourApiService tourApiService) {
        this.tourApiService = tourApiService;
    }

    public List<FestivalResponse> getFestivals(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        CacheEntry cached = cache.get(yearMonth);
        if (cached != null && !cached.isExpired()) {
            return cached.getFestivals();
        }

        String eventStartDate = yearMonth.atDay(1).format(YYYYMMDD);
        List<TourContentResponse> apiResults = tourApiService.getFestivals(eventStartDate, null, 100, 1);

        List<FestivalResponse> festivals = List.copyOf(
                apiResults.stream()
                        .map(tc -> FestivalResponse.builder()
                                .title(tc.getTitle())
                                .startDate(formatApiDate(tc.getEventStartDate()))
                                .endDate(formatApiDate(tc.getEventEndDate()))
                                .location(resolveLocalRegion(tc.getAreaCode(), tc.getAddr1()))
                                .build())
                        .toList()
        );

        cache.put(yearMonth, new CacheEntry(festivals, System.currentTimeMillis() + CACHE_TTL.toMillis()));
        return festivals;
    }

    // API 날짜 형식 "20260701" → "2026-07-01"
    private String formatApiDate(String raw) {
        if (raw == null || raw.length() != 8) return raw;
        return raw.substring(0, 4) + "-" + raw.substring(4, 6) + "-" + raw.substring(6, 8);
    }

    private String extractRegion(String addr) {
        if (addr == null || addr.isBlank()) return null;
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

    private String resolveLocalRegion(Integer areaCode, String addr) {
        if (areaCode == null) return extractRegion(addr);
        return switch (areaCode) {
            case 1  -> "서울";
            case 2  -> "인천";
            case 3  -> "대전";
            case 4  -> "대구";
            case 5  -> "광주";
            case 6  -> "부산";
            case 7  -> "울산";
            case 8  -> "세종";
            case 31 -> "경기";
            case 32, 35, 38 -> extractRegion(addr);
            case 33 -> "충북";
            case 34 -> "충남";
            case 36 -> "경남";
            case 37 -> "전주";
            case 39 -> "제주";
            default -> extractRegion(addr);
        };
    }

    private String normalizeRegionToken(String token) {
        if (token == null || token.isBlank()) return token;
        for (String suffix : List.of("특별자치도", "특별자치시", "광역시", "특별시", "시", "군", "구", "도")) {
            if (token.endsWith(suffix)) {
                return token.substring(0, token.length() - suffix.length());
            }
        }
        return token;
    }

    private static class CacheEntry {

        private final List<FestivalResponse> festivals;
        private final long expiresAtMillis;

        private CacheEntry(List<FestivalResponse> festivals, long expiresAtMillis) {
            this.festivals = festivals;
            this.expiresAtMillis = expiresAtMillis;
        }

        private List<FestivalResponse> getFestivals() {
            return festivals;
        }

        private boolean isExpired() {
            return System.currentTimeMillis() >= expiresAtMillis;
        }
    }
}
