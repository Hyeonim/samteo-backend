package com.samteo.domain.myplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalPlannerRequest {

    private String id;
    private String title;
    private String memo;
    private List<EventTypeDto> eventTypes;
    private List<ScheduleDto> schedule;

    private String regionName;
    private Long accommodationCost;
    private Long totalSalary;
    private Long disposableIncome;
    private Long fixedExpense;
    private Map<String, Object> accommodation;
    private List<Map<String, Object>> jobs;
}
