package com.samteo.domain.planner.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class JobPageResponse {
    private List<JobResponse> items;
    private int page;
    private int size;
    private long totalCount;
    private int totalPages;
    private boolean hasNext;
}
