package com.samteo.domain.region.dto.response;

import com.samteo.domain.region.entity.MetaRegion;
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

    public static RegionResponse from(MetaRegion region) {
        return RegionResponse.builder()
                .id(String.valueOf(region.getId()))
                .name(region.getName())
                .summary("체류할 지역을 선택해 주세요.")
                .desc("체류할 지역을 선택해 주세요.")
                .badge("REGION")
                .badgeType("blue")
                .bg("linear-gradient(160deg, #1e3a5f, #2d5a8e)")
                .tags(List.of())
                .recommendationReasons(List.of())
                .build();
    }
}
