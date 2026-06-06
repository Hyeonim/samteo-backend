package com.samteo.domain.authentication.service;

import com.samteo.domain.authentication.dto.response.KakaoUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public class KakaoOAuthService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret:}")
    private String clientSecret;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        if (clientSecret != null && !clientSecret.isBlank()) {
            params.add("client_secret", clientSecret);
        }
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        Map<String, Object> response;
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> tokenResponse = restTemplate.postForObject(
                    "https://kauth.kakao.com/oauth/token", request, Map.class);
            response = tokenResponse;
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(
                    "Kakao token request failed: status=" + e.getStatusCode()
                            + ", headers=" + e.getResponseHeaders()
                            + ", body=" + e.getResponseBodyAsString(),
                    e
            );
        }

        if (response == null || !response.containsKey("access_token")) {
            throw new RuntimeException("Kakao token request failed.");
        }
        return (String) response.get("access_token");
    }

    @SuppressWarnings("unchecked")
    public KakaoUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                Map.class);

        Map<String, Object> body = response.getBody();
        if (body == null) {
            throw new RuntimeException("Kakao user info request failed.");
        }

        Long id = ((Number) body.get("id")).longValue();
        Map<String, Object> kakaoAccount = body.get("kakao_account") instanceof Map<?, ?>
                ? (Map<String, Object>) body.get("kakao_account")
                : Collections.emptyMap();
        Map<String, Object> profile = kakaoAccount.get("profile") instanceof Map<?, ?>
                ? (Map<String, Object>) kakaoAccount.get("profile")
                : Collections.emptyMap();

        String email = asString(kakaoAccount.get("email"));
        if (email == null || email.isBlank()) {
            email = "kakao_" + id + "@no-email.local";
        }

        String nickname = asString(profile.get("nickname"));
        if (nickname == null || nickname.isBlank()) {
            nickname = "KakaoUser" + id;
        }

        return new KakaoUserInfo(id, email, nickname);
    }

    private String asString(Object value) {
        return value instanceof String stringValue ? stringValue : null;
    }
}
