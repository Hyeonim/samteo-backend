package com.samteo.domain.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminStatsResponse {
    private long totalUsers;
    private long totalPlanners;
    private long totalJobs;
    private long totalAccommodations;
}
