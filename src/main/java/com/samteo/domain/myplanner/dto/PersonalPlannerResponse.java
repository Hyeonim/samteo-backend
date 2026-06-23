package com.samteo.domain.myplanner.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class PersonalPlannerResponse {

    private String id;
    private String title;
    private String memo;
    private String updatedAt;
    private List<EventTypeDto> eventTypes;
    private List<ScheduleDto> schedule;

    private String regionName;
    private Long accommodationCost;
    private Long totalSalary;
    private Long disposableIncome;
    private Long fixedExpense;
    private Map<String, Object> accommodation;
    private List<Map<String, Object>> jobs;
    private String plannerType;
}
