package com.samteo.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String provider;
    private String providerToken;
    private String email;
    private String nickname;
}
