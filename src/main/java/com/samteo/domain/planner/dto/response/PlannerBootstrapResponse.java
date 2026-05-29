package com.samteo.domain.planner.dto.response;

import com.samteo.domain.region.dto.response.RegionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PlannerBootstrapResponse {

    private List<RegionResponse> regions;
    private List<JobResponse> jobs;
    private List<AccommodationResponse> accommodations;
    private MapProviderResponse mapProvider;
}
