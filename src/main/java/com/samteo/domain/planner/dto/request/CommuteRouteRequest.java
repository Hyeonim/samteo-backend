package com.samteo.domain.planner.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommuteRouteRequest {

    private String accommodationId;
    private String jobId;
    private Integer sort;
    private Integer searchType;
    private Integer pathType;
    private Integer language;
}
