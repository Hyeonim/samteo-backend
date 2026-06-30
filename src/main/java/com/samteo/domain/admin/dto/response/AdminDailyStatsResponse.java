package com.samteo.domain.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminDailyStatsResponse {
    private String date;
    private long users;
    private long planners;
    private long communityPosts;
}
