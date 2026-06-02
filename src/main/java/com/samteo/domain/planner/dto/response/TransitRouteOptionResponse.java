package com.samteo.domain.planner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TransitRouteOptionResponse {

    private int index;
    private int pathType;
    private String pathTypeName;
    private int totalTime;
    private int payment;
    private int totalWalkTime;
    private int totalDistance;
    private int busTransitCount;
    private int subwayTransitCount;
    private String firstStartStation;
    private String lastEndStation;
    private String mapObject;
    private List<TransitSubPathResponse> subPaths;
}
