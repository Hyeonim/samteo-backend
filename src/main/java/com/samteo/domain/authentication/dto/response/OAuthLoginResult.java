package com.samteo.domain.authentication.dto.response;

import lombok.Getter;

@Getter
public class OAuthLoginResult {

    private final AuthResponse auth;
    private final String linkToken;
    private final String provider;
    private final String email;

    private OAuthLoginResult(AuthResponse auth, String linkToken, String provider, String email) {
        this.auth = auth;
        this.linkToken = linkToken;
        this.provider = provider;
        this.email = email;
    }

    public static OAuthLoginResult authenticated(AuthResponse auth) {
        return new OAuthLoginResult(auth, null, null, null);
    }

    public static OAuthLoginResult linkRequired(String linkToken, String provider, String email) {
        return new OAuthLoginResult(null, linkToken, provider, email);
    }

    public boolean isLinkRequired() {
        return linkToken != null;
    }
}
