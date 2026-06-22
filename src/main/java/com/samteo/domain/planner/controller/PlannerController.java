package com.samteo.domain.planner.controller;

import com.samteo.domain.planner.service.PlannerService;
import com.samteo.domain.planner.dto.request.BudgetSimulationRequest;
import com.samteo.domain.planner.dto.request.CommuteRouteRequest;
import com.samteo.domain.planner.dto.request.PlannerCreateRequest;
import com.samteo.domain.planner.dto.request.TransitRouteRequest;
import com.samteo.domain.planner.dto.response.AccommodationResponse;
import com.samteo.domain.planner.dto.response.BudgetSimulationResponse;
import com.samteo.domain.planner.dto.response.JobResponse;
import com.samteo.domain.planner.dto.response.JobPageResponse;
import com.samteo.domain.planner.dto.response.MapProviderResponse;
import com.samteo.domain.planner.dto.response.PlannerBootstrapResponse;
import com.samteo.domain.planner.dto.response.PlannerResponse;
import com.samteo.domain.planner.dto.response.TransitRouteResponse;
import com.samteo.domain.planner.service.OdsayTransitService;
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
    private final OdsayTransitService odsayTransitService;

    @GetMapping("/bootstrap")
    public ResponseEntity<ApiResponse<PlannerBootstrapResponse>> getBootstrapData() {
        return ResponseEntity.ok(ApiResponse.success(plannerService.getBootstrapData()));
    }

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getJobs(
            @RequestParam(required = false) String regionId,
            @RequestParam(required = false) String cityId
    ) {
        return ResponseEntity.ok(ApiResponse.success(plannerService.getJobs(regionId, cityId)));
    }

    @GetMapping("/jobs/page")
    public ResponseEntity<ApiResponse<JobPageResponse>> getJobsPage(
            @RequestParam(required = false) String regionId,
            @RequestParam(required = false) String cityId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(plannerService.getJobsPage(regionId, cityId, page, size)));
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

    @PostMapping("/transit-routes")
    public ResponseEntity<ApiResponse<TransitRouteResponse>> searchTransitRoutes(
            @RequestBody TransitRouteRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(odsayTransitService.searchTransitRoutes(request)));
    }

    @PostMapping("/commute-route")
    public ResponseEntity<ApiResponse<TransitRouteResponse>> searchCommuteRoute(
            @RequestBody CommuteRouteRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(odsayTransitService.searchCommuteRoute(request)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PlannerResponse>> createPlanner(
            @RequestBody PlannerCreateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(plannerService.createPlanner(request)));
    }
}
