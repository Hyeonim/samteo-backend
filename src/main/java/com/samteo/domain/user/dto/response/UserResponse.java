package com.samteo.domain.user.dto.response;

import com.samteo.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {

    private Long userId;
    private String email;
    private String name;
    private String provider;
    private List<String> providers;
    private String role;

    public static UserResponse from(User user) {
        return from(user, List.of(user.getProvider()));
    }

    public static UserResponse from(User user, List<String> providers) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .provider(user.getProvider())
                .providers(providers)
                .role(user.getRole())
                .build();
    }
}
