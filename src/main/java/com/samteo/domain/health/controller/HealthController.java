package com.samteo.domain.health.controller;

import com.samteo.global.response.ApiResponse;
import com.samteo.domain.health.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 서버가 정상적으로 동작 중인지 확인하기 위한 경량 헬스체크 API를 제공한다.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HealthController {

    private final HealthService healthService;

    /**
     * 서버 기동 여부를 확인할 수 있는 간단한 상태 메시지를 반환한다.
     *
     * @return 서버 상태 메시지 응답 래퍼ㅅ
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success(healthService.getStatus()));
    }
}
