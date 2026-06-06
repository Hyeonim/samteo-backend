package com.samteo.domain.job.controller;

import com.samteo.domain.job.dto.response.JobResponse;
import com.samteo.domain.job.service.JobService;
import com.samteo.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 관광 일자리 데이터 소스를 기반으로 채용 공고 API를 제공한다.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    /**
     * 외부 관광 일자리 API에서 지정한 개수만큼 채용 공고를 조회한다.
     *
     * @param size 조회할 공고 개수
     * @return 채용 공고 목록 응답 래퍼
     */
    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getJobs(
            @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(ApiResponse.success(jobService.getJobs(size)));
    }
}
