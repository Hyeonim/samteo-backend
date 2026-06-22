package com.samteo.domain.festival.service;

import com.samteo.domain.festival.dto.response.FestivalResponse;
import com.samteo.domain.tourapi.dto.response.TourContentResponse;
import com.samteo.domain.tourapi.service.TourApiService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FestivalService {

    private static final int ATTRACTION_CONTENT_TYPE_ID = 12;
    private static final int FESTIVAL_CONTENT_TYPE_ID = 15;

    private final TourApiService tourApiService;

    public FestivalService(TourApiService tourApiService) {
        this.tourApiService = tourApiService;
    }

    public List<FestivalResponse> getFestivals(int numOfRows, int pageNo) {
        return getContents(FESTIVAL_CONTENT_TYPE_ID, "축제", numOfRows, pageNo);
    }

    public List<FestivalResponse> getAttractions(int numOfRows, int pageNo) {
        return getContents(ATTRACTION_CONTENT_TYPE_ID, "관광지", numOfRows, pageNo);
    }

    private List<FestivalResponse> getContents(
            int contentTypeId,
            String category,
            int numOfRows,
            int pageNo
    ) {
        List<TourContentResponse> apiResults = tourApiService.getSpots(
                null, contentTypeId, numOfRows, pageNo);

        List<FestivalResponse> festivals = List.copyOf(
                apiResults.stream()
                        .map(tc -> FestivalResponse.builder()
                                .id(String.valueOf(tc.getContentId()))
                                .contentTypeId(contentTypeId)
                                .category(category)
                                .title(tc.getTitle())
                                .startDate(null)
                                .endDate(null)
                                .location(resolveLocalRegion(tc.getAreaCode(), tc.getAddr1()))
                                .address(joinAddress(tc.getAddr1(), tc.getAddr2()))
                                .imageUrl(tc.getFirstImage())
                                .build())
                        .toList()
        );

        return festivals;
    }

    private String joinAddress(String addr1, String addr2) {
        return String.join(" ", addr1 == null ? "" : addr1, addr2 == null ? "" : addr2).trim();
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

}
