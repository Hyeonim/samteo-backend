package com.samteo.domain.myplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PersonalPlannerResponse {

    private String id;
    private String title;
    private String memo;
    private String updatedAt;
    private List<EventTypeDto> eventTypes;
    private List<ScheduleDto> schedule;
}
