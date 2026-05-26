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
    private String title;
    private String company;
    private String regionId;
    private String district;
    private String category;
    private String employmentType;
    private int monthlySalary;
    private String workingDays;
    private int commuteMinutes;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private List<String> tags;
    private String source;

    public static JobResponse from(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .company(job.getCompany())
                .regionId(job.getRegionId())
                .district(job.getDistrict())
                .category(job.getCategory())
                .employmentType(job.getEmploymentType())
                .monthlySalary(job.getMonthlySalary())
                .workingDays(job.getWorkingDays())
                .commuteMinutes(job.getCommuteMinutes())
                .latitude(job.getLatitude())
                .longitude(job.getLongitude())
                .tags(job.getTags())
                .source("HR_DUMMY")
                .build();
    }
}
