package com.samteo.domain.authentication.controller;

import com.samteo.domain.authentication.dto.request.OAuthLinkRequest;
import com.samteo.domain.authentication.dto.response.AuthResponse;
import com.samteo.domain.authentication.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me/auth-identities")
@RequiredArgsConstructor
public class OAuthLinkController {

    private final AuthService authService;

    @PostMapping("/link")
    public ResponseEntity<AuthResponse> link(
            @AuthenticationPrincipal Long userId,
            @RequestBody OAuthLinkRequest request
    ) {
        return ResponseEntity.ok(authService.linkOAuthIdentity(userId, request));
    }
}
