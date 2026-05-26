package com.samteo.domain.region.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Region {

    private String id;
    private String name;
    private String summary;
    private int hotPlaceScore;
    private int housingCostScore;
    private BigDecimal centerLatitude;
    private BigDecimal centerLongitude;
    private List<String> tags;
    private List<String> recommendationReasons;
}
