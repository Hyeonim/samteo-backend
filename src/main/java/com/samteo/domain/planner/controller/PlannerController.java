package com.samteo.domain.planner.controller;

import com.samteo.domain.planner.service.PlannerService;
import com.samteo.domain.planner.dto.request.BudgetSimulationRequest;
import com.samteo.domain.planner.dto.request.PlannerCreateRequest;
import com.samteo.domain.planner.dto.response.AccommodationResponse;
import com.samteo.domain.planner.dto.response.BudgetSimulationResponse;
import com.samteo.domain.planner.dto.response.JobResponse;
import com.samteo.domain.planner.dto.response.MapProviderResponse;
import com.samteo.domain.planner.dto.response.PlannerBootstrapResponse;
import com.samteo.domain.planner.dto.response.PlannerResponse;
import com.samteo.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/planner")
@RequiredArgsConstructor
public class PlannerController {

    private final PlannerService plannerService;

    @GetMapping("/bootstrap")
    public ResponseEntity<ApiResponse<PlannerBootstrapResponse>> getBootstrapData() {
        return ResponseEntity.ok(ApiResponse.success(plannerService.getBootstrapData()));
    }

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getJobs(
            @RequestParam(required = false) String regionId
    ) {
        return ResponseEntity.ok(ApiResponse.success(plannerService.getJobs(regionId)));
    }

    @GetMapping("/accommodations")
    public ResponseEntity<ApiResponse<List<AccommodationResponse>>> getAccommodations(
            @RequestParam(required = false) String regionId
    ) {
        return ResponseEntity.ok(ApiResponse.success(plannerService.getAccommodations(regionId)));
    }

    @GetMapping("/map-provider")
    public ResponseEntity<ApiResponse<MapProviderResponse>> getMapProvider() {
        return ResponseEntity.ok(ApiResponse.success(plannerService.getMapProvider()));
    }

    @PostMapping("/budget")
    public ResponseEntity<ApiResponse<BudgetSimulationResponse>> simulateBudget(
            @RequestBody BudgetSimulationRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(plannerService.simulateBudget(request)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PlannerResponse>> createPlanner(
            @RequestBody PlannerCreateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(plannerService.createPlanner(request)));
    }
}
