package com.samteo.domain.admin.dto.response;

import com.samteo.domain.planner.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AdminJobResponse {
    private String id;
    private String title;
    private String company;
    private String cityName;
    private String district;
    private String category;
    private String employmentType;
    private int monthlySalary;
    private String workingDays;
    private int bookmarkCount;
    private int reviewCount;
    private List<String> tags;

    public static AdminJobResponse from(Job job) {
        return AdminJobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .company(job.getCompany())
                .cityName(job.getCityName())
                .district(job.getDistrict())
                .category(job.getCategory())
                .employmentType(job.getEmploymentType())
                .monthlySalary(job.getMonthlySalary())
                .workingDays(job.getWorkingDays())
                .bookmarkCount(job.getBookmarkCount())
                .reviewCount(job.getReviewCount())
                .tags(job.getTags())
                .build();
    }
}
