package com.samteo.domain.planner.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransitRouteRequest {

    private String startAnchorType;
    private String startAnchorId;
    private String endAnchorType;
    private String endAnchorId;
    private String startName;
    private String endName;
    private BigDecimal startLatitude;
    private BigDecimal startLongitude;
    private BigDecimal endLatitude;
    private BigDecimal endLongitude;
    private Integer sort;
    private Integer searchType;
    private Integer pathType;
    private Integer language;
}
