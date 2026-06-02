package com.samteo.domain.planner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TransitSubPathResponse {

    private int trafficType;
    private String trafficTypeName;
    private int sectionTime;
    private int distance;
    private int stationCount;
    private String startName;
    private String endName;
    private List<String> lanes;
}
