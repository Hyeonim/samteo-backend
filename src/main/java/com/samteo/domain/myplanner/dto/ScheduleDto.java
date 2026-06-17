package com.samteo.domain.myplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {

    private String id;
    private String title;
    private int day;
    private String start;
    private String end;
    private String type;
    private String typeLabel;
    private String color;
    private String memo;
    private String dateKey;
    private String repeatMode;
    private boolean locked;
}
