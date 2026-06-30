package com.samteo.domain.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class AdminStatsResponse {
    private long totalUsers;
    private long totalPlanners;
    private long totalJobs;
    private long totalAccommodations;
    private long totalCommunityPosts;
    private int periodDays;
    private List<AdminDailyStatsResponse> dailyStats;
}
