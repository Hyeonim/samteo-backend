package com.samteo.domain.health.controller;

import com.samteo.global.response.ApiResponse;
import com.samteo.global.response.JobResponse;
import com.samteo.domain.health.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getJobs(
            @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(ApiResponse.success(jobService.getJobs(size)));
    }
}
