package com.samteo.controller;

import com.samteo.dto.response.ApiResponse;
import com.samteo.dto.response.JobResponse;
import com.samteo.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
