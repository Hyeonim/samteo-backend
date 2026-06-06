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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(ApiResponse.success(userService.getMe(userId)));
    }
}
