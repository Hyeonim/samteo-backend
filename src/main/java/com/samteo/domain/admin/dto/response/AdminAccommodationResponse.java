package com.samteo.domain.admin.dto.response;

import com.samteo.domain.planner.entity.Accommodation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AdminAccommodationResponse {
    private String id;
    private String name;
    private String regionId;
    private String district;
    private String address;
    private int monthlyPrice;
    private int deposit;
    private BigDecimal distanceKm;
    private int commuteMinutes;
    private List<String> tags;

    public static AdminAccommodationResponse from(Accommodation acc) {
        return AdminAccommodationResponse.builder()
                .id(acc.getId())
                .name(acc.getName())
                .regionId(acc.getRegionId())
                .district(acc.getDistrict())
                .address(acc.getAddress())
                .monthlyPrice(acc.getMonthlyPrice())
                .deposit(acc.getDeposit())
                .distanceKm(acc.getDistanceKm())
                .commuteMinutes(acc.getCommuteMinutes())
                .tags(acc.getTags())
                .build();
    }
}
