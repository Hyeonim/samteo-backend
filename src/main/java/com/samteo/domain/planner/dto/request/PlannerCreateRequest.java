package com.samteo.domain.planner.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlannerCreateRequest {

    private String regionId;
    private List<String> jobIds;
    private String accommodationId;
    private LocalDate startDate;
    private Integer months;
    private Integer transportCost;
    private Integer foodCost;
    private Integer extraCost;
}
