package com.samteo.domain.region.dto.response;

import com.samteo.domain.region.entity.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RegionResponse {

    private String id;
    private String name;
    private String summary;
    private String desc;
    private String badge;
    private String badgeType;
    private String bg;
    private int hotPlaceScore;
    private int housingCostScore;
    private BigDecimal centerLatitude;
    private BigDecimal centerLongitude;
    private List<String> tags;
    private List<String> recommendationReasons;

    public static RegionResponse from(Region region) {
        return RegionResponse.builder()
                .id(region.getId())
                .name(region.getName())
                .summary(region.getSummary())
                .desc(region.getSummary())
                .badge(region.getHotPlaceScore() >= 90 ? "BEST" : "RECOMMENDED")
                .badgeType(region.getHotPlaceScore() >= 90 ? "blue" : "green")
                .bg("linear-gradient(160deg, #1e3a5f, #2d5a8e)")
                .hotPlaceScore(region.getHotPlaceScore())
                .housingCostScore(region.getHousingCostScore())
                .centerLatitude(region.getCenterLatitude())
                .centerLongitude(region.getCenterLongitude())
                .tags(region.getTags())
                .recommendationReasons(region.getRecommendationReasons())
                .build();
    }
}
