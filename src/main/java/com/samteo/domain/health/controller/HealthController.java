package com.samteo.domain.health.controller;

import com.samteo.global.response.ApiResponse;
import com.samteo.domain.health.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthController {

    private final HealthService healthService;

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success(healthService.getStatus()));
    }
}
