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
    private int price;
    private int deposit;
    private BigDecimal distanceKm;
    private int commuteMinutes;
    private String location;
    private String pos;
    private String posType;
    private String bg;
    private String color;
    private Integer rank;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal lat;
    private BigDecimal lng;
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
                .price(accommodation.getMonthlyPrice())
                .deposit(accommodation.getDeposit())
                .distanceKm(accommodation.getDistanceKm())
                .commuteMinutes(accommodation.getCommuteMinutes())
                .location(accommodation.getDistrict() + " · " + accommodation.getAddress())
                .pos("▲ 가처분 계산 중")
                .posType("pos")
                .bg("linear-gradient(135deg,#1e3a5f,#2d5a8e)")
                .color(accommodation.getColor() != null ? accommodation.getColor() : "#3B82F6")
                .rank(accommodation.getRank())
                .latitude(accommodation.getLatitude())
                .longitude(accommodation.getLongitude())
                .lat(accommodation.getLatitude())
                .lng(accommodation.getLongitude())
                .tags(accommodation.getTags())
                .source("DB")
                .build();
    }
}
