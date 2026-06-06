package com.samteo.domain.authentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 인증 완료 후 사용자 식별 정보와 발급된 JWT를 함께 전달하는 응답 객체이다.
 */
@Getter
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private Long userId;
    private String email;
    private String name;
}
