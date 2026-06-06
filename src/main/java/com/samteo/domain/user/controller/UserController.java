package com.samteo.domain.user.controller;

import com.samteo.domain.user.dto.response.UserResponse;
import com.samteo.domain.user.service.UserService;
import com.samteo.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 현재 인증된 사용자 프로필과 관련된 API를 제공한다.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 현재 인증된 사용자의 프로필 정보를 반환한다.
     *
     * @param userId 보안 컨텍스트에서 주입된 인증 사용자 ID
     * @return 사용자 프로필 응답 래퍼
     */
    @GetMapping("/users/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getMe(userId)));
    }
}
