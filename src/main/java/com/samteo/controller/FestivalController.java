package com.samteo.controller;

import com.samteo.dto.response.ApiResponse;
import com.samteo.dto.response.FestivalResponse;
import com.samteo.service.FestivalService;
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
public class FestivalController {

    private final FestivalService festivalService;

    @GetMapping("/festivals")
    public ResponseEntity<ApiResponse<List<FestivalResponse>>> getFestivals(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(ApiResponse.success(festivalService.getFestivals(year, month)));
    }
}
