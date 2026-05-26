package com.samteo.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {

    private String tokenType;
    private String accessToken;
    private long expiresIn;
    private UserResponse user;
}
