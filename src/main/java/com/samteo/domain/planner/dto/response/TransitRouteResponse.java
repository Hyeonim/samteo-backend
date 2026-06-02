package com.samteo.domain.planner.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TransitRouteResponse {

    private String provider;
    private TransitAnchorResponse origin;
    private TransitAnchorResponse destination;
    private List<TransitRouteOptionResponse> routes;
    private List<String> notices;
    private JsonNode raw;
}
