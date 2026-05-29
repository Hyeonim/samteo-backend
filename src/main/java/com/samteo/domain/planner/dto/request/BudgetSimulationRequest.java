package com.samteo.domain.planner.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetSimulationRequest {

    private String jobId;
    private String accommodationId;
    private Integer transportCost;
    private Integer foodCost;
    private Integer extraCost;
    private Boolean useRecommendedRoute;
}
