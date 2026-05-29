package com.samteo.domain.planner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class CalendarEventResponse {

    private LocalDate date;
    private String type;
    private String title;
    private String description;
}
