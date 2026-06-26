package com.samteo.domain.user.controller;

import com.samteo.domain.user.dto.request.UserProfileUpdateRequest;
import com.samteo.domain.user.dto.response.UserProfileResponse;
import com.samteo.domain.user.dto.response.UserResponse;
import com.samteo.domain.user.service.UserService;
import com.samteo.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PutMapping("/users/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMe(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserProfileUpdateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(userService.updateMe(userId, request)));
    }

    @GetMapping("/users/{profileUserId}/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(
            @AuthenticationPrincipal Long viewerId,
            @PathVariable Long profileUserId
    ) {
        return ResponseEntity.ok(ApiResponse.success(userService.getProfile(profileUserId, viewerId)));
    }

    @PostMapping("/users/{profileUserId}/follow")
    public ResponseEntity<ApiResponse<UserProfileResponse>> follow(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long profileUserId
    ) {
        return ResponseEntity.ok(ApiResponse.success(userService.follow(userId, profileUserId)));
    }

    @DeleteMapping("/users/{profileUserId}/follow")
    public ResponseEntity<ApiResponse<UserProfileResponse>> unfollow(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long profileUserId
    ) {
        return ResponseEntity.ok(ApiResponse.success(userService.unfollow(userId, profileUserId)));
    }
}
