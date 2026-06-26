package com.samteo.domain.authentication.service;

import com.samteo.domain.authentication.dto.response.GoogleUserInfo;
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

import java.util.Map;

@Service
public class GoogleOAuthService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        Map<String, Object> response;
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> tokenResponse = restTemplate.postForObject(
                    "https://oauth2.googleapis.com/token", request, Map.class);
            response = tokenResponse;
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(
                    "Google token request failed: status=" + e.getStatusCode()
                            + ", body=" + e.getResponseBodyAsString(),
                    e
            );
        }

        if (response == null || !response.containsKey("access_token")) {
            throw new RuntimeException("Google token request failed.");
        }
        return (String) response.get("access_token");
    }

    public GoogleUserInfo getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        );

        Map<String, Object> body = response.getBody();
        if (body == null) {
            throw new RuntimeException("Google user info request failed.");
        }

        String id = asString(body.get("id"));
        if (id == null || id.isBlank()) {
            throw new RuntimeException("Google user id is missing.");
        }

        String email = asString(body.get("email"));
        if (email == null || email.isBlank()) {
            email = "google_" + id + "@no-email.local";
        }

        String name = asString(body.get("name"));
        if (name == null || name.isBlank()) {
            name = "GoogleUser" + id;
        }

        return new GoogleUserInfo(id, email, name);
    }

    private String asString(Object value) {
        return value instanceof String stringValue ? stringValue : null;
    }
}
