package com.samteo.domain.festival.controller;

import com.samteo.domain.festival.dto.response.FestivalResponse;
import com.samteo.domain.festival.service.FestivalService;
import com.samteo.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TourAPI 축제 데이터를 기반으로 축제 조회 API를 제공한다.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;

    /**
     * 요청한 연도와 월에 해당하는 축제 목록을 조회한다.
     *
     * @param year 조회 대상 연도
     * @param month 조회 대상 월
     * @return 축제 목록 응답 래퍼
     */
    @GetMapping("/festivals")
    public ResponseEntity<ApiResponse<List<FestivalResponse>>> getFestivals(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(ApiResponse.success(festivalService.getFestivals(year, month)));
    }
}
