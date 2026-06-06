package com.samteo.domain.user.dto.response;

import com.samteo.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {

    private Long userId;
    private String email;
    private String name;
    private String provider;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .provider(user.getProvider())
                .build();
    }
}
