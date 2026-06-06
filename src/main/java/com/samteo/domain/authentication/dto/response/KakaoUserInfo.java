package com.samteo.domain.authentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 카카오 프로필 API에서 조회한 사용자 정보를 정규화하여 보관한다.
 */
@Getter
@AllArgsConstructor
public class KakaoUserInfo {

    private Long id;
    private String email;
    private String nickname;
}
