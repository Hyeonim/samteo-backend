package com.samteo.domain.user.controller;

import com.samteo.domain.user.dto.request.LoginRequest;
import com.samteo.domain.user.dto.response.LoginResponse;
import com.samteo.domain.user.dto.response.UserResponse;
import com.samteo.domain.user.service.UserService;
import com.samteo.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(userService.login(request)));
    }

    @GetMapping("/users/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe() {
        return ResponseEntity.ok(ApiResponse.success(userService.getMe()));
    }
}
