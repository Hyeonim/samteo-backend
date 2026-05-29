package com.samteo.domain.planner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class BudgetSimulationResponse {

    private int income;
    private int housingCost;
    private int transportCost;
    private int foodCost;
    private int extraCost;
    private int balance;
    private List<String> notes;
}
