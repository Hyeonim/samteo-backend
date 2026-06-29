package com.samteo.domain.authentication.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OAuthLinkRequest {
    private String linkToken;

    public OAuthLinkRequest(String linkToken) {
        this.linkToken = linkToken;
    }
}
