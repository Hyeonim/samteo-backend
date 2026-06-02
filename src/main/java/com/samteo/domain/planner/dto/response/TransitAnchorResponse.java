package com.samteo.domain.planner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class TransitAnchorResponse {

    private String type;
    private String id;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
