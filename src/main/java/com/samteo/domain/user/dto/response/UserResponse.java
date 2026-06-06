package com.samteo.domain.user.dto.response;

import com.samteo.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * API 클라이언트에 반환할 인증 사용자 프로필 응답 데이터를 표현한다.
 */
@Getter
@Builder
@AllArgsConstructor
public class UserResponse {

    private Long userId;
    private String email;
    private String name;
    private String provider;

    /**
     * 사용자 엔티티를 API 응답용 프로필 데이터로 변환한다.
     *
     * @param user 도메인 사용자 엔티티
     * @return 사용자 응답 데이터
     */
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .provider(user.getProvider())
                .build();
    }
}
