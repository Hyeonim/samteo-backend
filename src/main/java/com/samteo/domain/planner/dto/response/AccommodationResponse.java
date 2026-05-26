package com.samteo.domain.planner.dto.response;

import com.samteo.domain.planner.entity.Accommodation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AccommodationResponse {

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
    private String source;

    public static AccommodationResponse from(Accommodation accommodation) {
        return AccommodationResponse.builder()
                .id(accommodation.getId())
                .name(accommodation.getName())
                .regionId(accommodation.getRegionId())
                .district(accommodation.getDistrict())
                .address(accommodation.getAddress())
                .monthlyPrice(accommodation.getMonthlyPrice())
                .deposit(accommodation.getDeposit())
                .distanceKm(accommodation.getDistanceKm())
                .commuteMinutes(accommodation.getCommuteMinutes())
                .latitude(accommodation.getLatitude())
                .longitude(accommodation.getLongitude())
                .tags(accommodation.getTags())
                .source("HR_DUMMY")
                .build();
    }
}
