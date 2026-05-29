package com.samteo.domain.user.dto.response;

import com.samteo.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String email;
    private String nickname;
    private String provider;
    private String profileImageUrl;
    private String role;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .provider(user.getProvider())
                .profileImageUrl(user.getProfileImageUrl())
                .role(user.getRole())
                .build();
    }
}
