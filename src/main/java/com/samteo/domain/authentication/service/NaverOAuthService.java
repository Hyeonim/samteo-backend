package com.samteo.domain.authentication.service;

import com.samteo.domain.authentication.dto.response.NaverUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
public class NaverOAuthService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    @Value("${naver.redirect.uri}")
    private String redirectUri;

    public String getAccessToken(String code, String state) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        if (state != null && !state.isBlank()) {
            params.add("state", state);
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        Map<String, Object> response;
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> tokenResponse = restTemplate.postForObject(
                    "https://nid.naver.com/oauth2.0/token", request, Map.class);
            response = tokenResponse;
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(
                    "Naver token request failed: status=" + e.getStatusCode()
                            + ", body=" + e.getResponseBodyAsString(),
                    e
            );
        }

        if (response == null || !response.containsKey("access_token")) {
            throw new RuntimeException("Naver token request failed.");
        }
        return (String) response.get("access_token");
    }

    @SuppressWarnings("unchecked")
    public NaverUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        );

        Map<String, Object> body = response.getBody();
        if (body == null) {
            throw new RuntimeException("Naver user info request failed.");
        }

        Map<String, Object> profile = body.get("response") instanceof Map<?, ?>
                ? (Map<String, Object>) body.get("response")
                : Collections.emptyMap();

        String id = asString(profile.get("id"));
        if (id == null || id.isBlank()) {
            throw new RuntimeException("Naver user id is missing.");
        }

        String email = asString(profile.get("email"));
        if (email == null || email.isBlank()) {
            email = "naver_" + id + "@no-email.local";
        }

        String name = asString(profile.get("name"));
        if (name == null || name.isBlank()) {
            name = asString(profile.get("nickname"));
        }
        if (name == null || name.isBlank()) {
            name = "NaverUser" + id;
        }

        return new NaverUserInfo(id, email, name);
    }

    private String asString(Object value) {
        return value instanceof String stringValue ? stringValue : null;
    }
}
