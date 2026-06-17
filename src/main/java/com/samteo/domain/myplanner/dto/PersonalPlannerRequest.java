package com.samteo.domain.myplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalPlannerRequest {

    private String id;
    private String title;
    private String memo;
    private List<EventTypeDto> eventTypes;
    private List<ScheduleDto> schedule;
}
