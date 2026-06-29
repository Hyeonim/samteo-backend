package com.samteo.domain.authentication.controller;

import com.samteo.domain.authentication.dto.response.GoogleUserInfo;
import com.samteo.domain.authentication.dto.response.KakaoUserInfo;
import com.samteo.domain.authentication.dto.response.NaverUserInfo;
import com.samteo.domain.authentication.dto.response.OAuthLoginResult;
import com.samteo.domain.authentication.service.AuthService;
import com.samteo.domain.authentication.service.GoogleOAuthService;
import com.samteo.domain.authentication.service.KakaoOAuthService;
import com.samteo.domain.authentication.service.NaverOAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * 외부 소셜 로그인 콜백을 처리하여 인증을 완료하는 컨트롤러이다.
 */
@RestController
@RequiredArgsConstructor
public class OauthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final GoogleOAuthService googleOAuthService;
    private final NaverOAuthService naverOAuthService;
    private final AuthService authService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    /**
     * 카카오 인가 코드를 액세스 토큰으로 교환한 뒤 사용자를 조회 또는 생성하고,
     * JWT를 포함한 프론트엔드 콜백 주소로 리다이렉트한다.
     *
     * @param code 카카오 인가 코드
     * @param response 프론트엔드 리다이렉트에 사용할 서블릿 응답 객체
     * @throws IOException 리다이렉트 응답 작성에 실패한 경우
     */
    @GetMapping("/login/oauth2/code/kakao")
    public void kakaoCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        String accessToken = kakaoOAuthService.getAccessToken(code);
        KakaoUserInfo userInfo = kakaoOAuthService.getUserInfo(accessToken);
        OAuthLoginResult result = authService.loginOrRegisterOAuth(
                "kakao",
                String.valueOf(userInfo.getId()),
                userInfo.getEmail(),
                userInfo.getNickname()
        );
        sendOAuthResult(response, result);
    }

    @GetMapping("/login/oauth2/code/google")
    public void googleCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        String accessToken = googleOAuthService.getAccessToken(code);
        GoogleUserInfo userInfo = googleOAuthService.getUserInfo(accessToken);
        OAuthLoginResult result = authService.loginOrRegisterOAuth(
                "google",
                userInfo.getId(),
                userInfo.getEmail(),
                userInfo.getName()
        );
        sendOAuthResult(response, result);
    }

    @GetMapping("/login/oauth2/code/naver")
    public void naverCallback(
            @RequestParam("code") String code,
            @RequestParam(value = "state", required = false) String state,
            HttpServletResponse response
    ) throws IOException {
        String accessToken = naverOAuthService.getAccessToken(code, state);
        NaverUserInfo userInfo = naverOAuthService.getUserInfo(accessToken);
        OAuthLoginResult result = authService.loginOrRegisterOAuth(
                "naver",
                userInfo.getId(),
                userInfo.getEmail(),
                userInfo.getName()
        );
        sendOAuthResult(response, result);
    }

    private void sendOAuthResult(HttpServletResponse response, OAuthLoginResult result) throws IOException {
        UriComponentsBuilder redirect = UriComponentsBuilder
                .fromUriString(frontendUrl)
                .path("/oauth/callback");
        if (result.isLinkRequired()) {
            redirect.queryParam("linkToken", result.getLinkToken())
                    .queryParam("provider", result.getProvider())
                    .queryParam("email", result.getEmail());
        } else {
            redirect.queryParam("token", result.getAuth().getToken());
        }
        response.sendRedirect(redirect.build().encode().toUriString());
    }
}
