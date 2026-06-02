package com.samteo.domain.planner.controller;

import com.samteo.domain.planner.service.PlannerService;
import com.samteo.domain.planner.dto.request.BudgetSimulationRequest;
import com.samteo.domain.planner.dto.request.CommuteRouteRequest;
import com.samteo.domain.planner.dto.request.PlannerCreateRequest;
import com.samteo.domain.planner.dto.request.TransitRouteRequest;
import com.samteo.domain.planner.dto.response.AccommodationResponse;
import com.samteo.domain.planner.dto.response.BudgetSimulationResponse;
import com.samteo.domain.planner.dto.response.JobResponse;
import com.samteo.domain.planner.dto.response.MapProviderResponse;
import com.samteo.domain.planner.dto.response.PlannerBootstrapResponse;
import com.samteo.domain.planner.dto.response.PlannerResponse;
import com.samteo.domain.planner.dto.response.TransitRouteResponse;
import com.samteo.domain.planner.service.OdsayTransitService;
import com.samteo.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${external.odsay.api-key:}")
    private String odsayApiKey;

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

    @GetMapping("/load-lane")
    public ResponseEntity<String> loadLane(@RequestParam String mapObject) {
        try {
            String encoded = java.net.URLEncoder.encode("0:0@" + mapObject, java.nio.charset.StandardCharsets.UTF_8);
            java.net.URI uri = java.net.URI.create(
                "https://api.odsay.com/v1/api/loadLane?mapObject=" + encoded + "&apiKey=" + odsayApiKey
            );
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest req = java.net.http.HttpRequest.newBuilder(uri).GET().build();
            java.net.http.HttpResponse<String> resp = client.send(req, java.net.http.HttpResponse.BodyHandlers.ofString());
            return ResponseEntity.ok().header("Content-Type", "application/json").body(resp.body());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\":\"loadLane failed\"}");
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PlannerResponse>> createPlanner(
            @RequestBody PlannerCreateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(plannerService.createPlanner(request)));
    }
}
