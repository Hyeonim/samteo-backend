package com.samteo.domain.planner.dto.response;

import com.samteo.domain.planner.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class JobResponse {

    private String id;
    private String name;
    private String title;
    private String company;
    private String regionId;
    private String district;
    private String region;
    private String type;
    private String category;
    private String employmentType;
    private int monthlySalary;
    private int salary;
    private String workingDays;
    private int commuteMinutes;
    private String desc;
    private String location;
    private String priceLabel;
    private String unit;
    private String sub;
    private String emoji;
    private boolean best;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal lat;
    private BigDecimal lng;
    private List<String> tags;
    private String source;

    public static JobResponse from(Job job) {
        String priceLabel = "₩" + String.format("%,.0f", job.getMonthlySalary() / 10000.0) + "만";
        return JobResponse.builder()
                .id(job.getId())
                .name(job.getTitle())
                .title(job.getTitle())
                .company(job.getCompany())
                .regionId(job.getRegionId())
                .district(job.getDistrict())
                .region(job.getRegionId())
                .type(job.getCategory())
                .category(job.getCategory())
                .employmentType(job.getEmploymentType())
                .monthlySalary(job.getMonthlySalary())
                .salary(job.getMonthlySalary())
                .workingDays(job.getWorkingDays())
                .commuteMinutes(job.getCommuteMinutes())
                .desc(job.getTitle())
                .location("📍 " + job.getDistrict())
                .priceLabel(priceLabel)
                .unit("/월")
                .sub("단기 알바")
                .emoji(job.getEmoji() != null ? job.getEmoji() : "💼")
                .best(job.isBest())
                .latitude(job.getLatitude())
                .longitude(job.getLongitude())
                .lat(job.getLatitude())
                .lng(job.getLongitude())
                .tags(job.getTags())
                .source("DB")
                .build();
    }
}
