package com.samteo.domain.authentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleUserInfo {

    private String id;
    private String email;
    private String name;
}
