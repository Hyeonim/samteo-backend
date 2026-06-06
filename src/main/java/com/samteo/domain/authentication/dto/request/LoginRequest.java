package com.samteo.domain.authentication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로컬 이메일/비밀번호 로그인에 필요한 요청 데이터를 표현한다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String email;
    private String password;
}
