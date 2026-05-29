package com.samteo.domain.health.controller;

import com.samteo.global.response.ApiResponse;
import com.samteo.global.response.FestivalResponse;
import com.samteo.domain.health.service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;

    @GetMapping("/festivals")
    public ResponseEntity<ApiResponse<List<FestivalResponse>>> getFestivals(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(ApiResponse.success(festivalService.getFestivals(year, month)));
    }
}
