package com.samteo.domain.planner.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Job {

    private String id;
    private String title;
    private String company;
    private String regionId;
    private String district;
    private String category;
    private String employmentType;
    private int monthlySalary;
    private String workingDays;
    private int commuteMinutes;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private List<String> tags;
}
