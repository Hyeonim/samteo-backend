package com.samteo.domain.authentication.controller;

import com.samteo.domain.authentication.dto.request.LoginRequest;
import com.samteo.domain.authentication.dto.request.RegisterRequest;
import com.samteo.domain.authentication.dto.response.AuthResponse;
import com.samteo.domain.authentication.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 이메일/비밀번호 기반 회원가입과 로그인을 처리하는 인증 API를 제공한다.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 로컬 계정을 신규 생성하고 즉시 사용할 수 있는 인증 결과를 반환한다.
     *
     * @param request 회원가입 요청 정보
     * @return 생성된 계정의 인증 응답
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * 이메일과 비밀번호로 로컬 사용자를 인증한다.
     *
     * @param request 로그인 요청 정보
     * @return 발급된 JWT를 포함한 인증 응답
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
