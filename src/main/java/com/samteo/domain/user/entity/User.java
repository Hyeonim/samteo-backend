package com.samteo.domain.user.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    private Long id;
    private String email;
    private String nickname;
    private String provider;
    private String providerId;
    private String profileImageUrl;
    private String role;
}
