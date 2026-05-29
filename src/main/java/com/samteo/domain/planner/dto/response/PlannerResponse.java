package com.samteo.domain.planner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PlannerResponse {

    private String plannerId;
    private String regionName;
    private List<String> jobNames;
    private String accommodationName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BudgetSimulationResponse budget;
    private List<CalendarEventResponse> calendar;
    private String shareUrl;
}
