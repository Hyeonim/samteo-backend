package com.samteo.domain.myplanner.controller;

import com.samteo.domain.myplanner.dto.PersonalPlannerRequest;
import com.samteo.domain.myplanner.dto.PersonalPlannerResponse;
import com.samteo.domain.myplanner.service.PersonalPlannerService;
import com.samteo.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 개인 플래너 CRUD API 엔드포인트를 제공하는 컨트롤러이다.
 */
@RestController
@RequestMapping("/api/my-planner")
@RequiredArgsConstructor
public class PersonalPlannerController {

    private final PersonalPlannerService personalPlannerService;

    /**
     * 현재 로그인 사용자의 플래너 목록을 전체 조회한다.
     *
     * @param userId 보안 컨텍스트에서 주입된 인증 사용자 ID
     * @return 플래너 목록 응답
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PersonalPlannerResponse>>> getAll(
            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(ApiResponse.success(personalPlannerService.getAll(userId)));
    }

    /**
     * 새로운 개인 플래너를 생성한다.
     *
     * @param userId 보안 컨텍스트에서 주입된 인증 사용자 ID
     * @param req    생성 요청 본문
     * @return 생성된 플래너 응답 (HTTP 201)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PersonalPlannerResponse>> create(
            @AuthenticationPrincipal Long userId,
            @RequestBody PersonalPlannerRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(personalPlannerService.create(userId, req)));
    }

    /**
     * 기존 플래너의 내용을 수정한다.
     *
     * @param userId 보안 컨텍스트에서 주입된 인증 사용자 ID
     * @param id     수정할 플래너 UUID
     * @param req    수정 요청 본문
     * @return 수정된 플래너 응답
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PersonalPlannerResponse>> update(
            @AuthenticationPrincipal Long userId,
            @PathVariable String id,
            @RequestBody PersonalPlannerRequest req) {
        return ResponseEntity.ok(ApiResponse.success(personalPlannerService.update(userId, id, req)));
    }

    /**
     * 플래너를 삭제한다.
     *
     * @param userId 보안 컨텍스트에서 주입된 인증 사용자 ID
     * @param id     삭제할 플래너 UUID
     * @return HTTP 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal Long userId,
            @PathVariable String id) {
        personalPlannerService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
