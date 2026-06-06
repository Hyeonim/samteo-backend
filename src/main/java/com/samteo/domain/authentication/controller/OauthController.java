package com.samteo.domain.authentication.controller;

import com.samteo.domain.authentication.dto.response.AuthResponse;
import com.samteo.domain.authentication.dto.response.KakaoUserInfo;
import com.samteo.domain.authentication.service.AuthService;
import com.samteo.domain.authentication.service.KakaoOAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class OauthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final AuthService authService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @GetMapping("/login/oauth2/code/kakao")
    public void kakaoCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        String accessToken = kakaoOAuthService.getAccessToken(code);
        KakaoUserInfo userInfo = kakaoOAuthService.getUserInfo(accessToken);
        AuthResponse auth = authService.loginOrRegisterOAuth(
                "kakao",
                String.valueOf(userInfo.getId()),
                userInfo.getEmail(),
                userInfo.getNickname()
        );
        response.sendRedirect(frontendUrl + "/oauth/callback?token=" + auth.getToken());
    }
}
