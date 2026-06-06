package com.samteo.domain.authentication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로컬 사용자 계정 생성에 필요한 요청 데이터를 표현한다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String email;
    private String password;
    private String name;
}
