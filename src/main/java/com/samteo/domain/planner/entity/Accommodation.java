package com.samteo.domain.planner.entity;

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
public class Accommodation {

    private String id;
    private String name;
    private String regionId;
    private String district;
    private String address;
    private int monthlyPrice;
    private int deposit;
    private BigDecimal distanceKm;
    private int commuteMinutes;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private List<String> tags;
}
