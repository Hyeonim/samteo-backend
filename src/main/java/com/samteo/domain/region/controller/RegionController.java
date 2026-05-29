package com.samteo.domain.region.controller;

import com.samteo.domain.region.service.RegionService;
import com.samteo.domain.region.dto.request.RegionRecommendationRequest;
import com.samteo.domain.region.dto.response.RegionResponse;
import com.samteo.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RegionResponse>>> getRegions() {
        return ResponseEntity.ok(ApiResponse.success(regionService.getRegions()));
    }

    @PostMapping("/recommendations")
    public ResponseEntity<ApiResponse<List<RegionResponse>>> recommendRegions(
            @RequestBody(required = false) RegionRecommendationRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(regionService.recommend(request)));
    }
}
