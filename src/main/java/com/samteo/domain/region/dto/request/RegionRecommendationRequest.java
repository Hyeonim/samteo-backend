package com.samteo.domain.region.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegionRecommendationRequest {

    private String option;
    private String district;
    private List<String> needs;
}
