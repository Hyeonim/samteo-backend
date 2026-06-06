package com.samteo.domain.authentication.service;

import com.samteo.domain.authentication.dto.request.LoginRequest;
import com.samteo.domain.authentication.dto.request.RegisterRequest;
import com.samteo.domain.authentication.dto.response.AuthResponse;
import com.samteo.domain.user.entity.User;
import com.samteo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 공통 사용자 도메인 모델을 기준으로 로컬 인증과 카카오 인증을 처리한다.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 이메일 중복 여부를 확인하고 비밀번호를 해시한 뒤 로컬 계정을 생성한다.
     *
     * @param request 로컬 회원가입 요청 정보
     * @return 생성된 사용자에 대한 JWT 포함 인증 응답
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmailIgnoreCase(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use.");
        }

        String hash = passwordEncoder.encode(request.getPassword());
        User user = userRepository.save(User.createLocal(request.getEmail(), request.getName(), hash));
        return toAuthResponse(user);
    }

    /**
     * 이메일과 비밀번호를 검증하여 기존 로컬 계정을 인증한다.
     *
     * @param request 로그인 요청 정보
     * @return JWT가 포함된 인증 응답
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));
        if (user.getPasswordHash() == null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password.");
        }
        return toAuthResponse(user);
    }

    /**
     * OAuth 사용자를 로그인 처리하거나, 최초 소셜 로그인인 경우 계정을 생성한다.
     *
     * @param provider OAuth 제공자 이름
     * @param providerId 제공자가 발급한 사용자 고유 식별자
     * @param email 제공자에서 내려준 사용자 이메일
     * @param name 제공자에서 내려준 사용자 표시 이름
     * @return JWT가 포함된 인증 응답
     */
    @Transactional
    public AuthResponse loginOrRegisterOAuth(String provider, String providerId, String email, String name) {
        User user = userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> userRepository.save(User.createOAuth(email, name, provider, providerId)));
        return toAuthResponse(user);
    }

    private AuthResponse toAuthResponse(User user) {
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getUserId(), user.getEmail(), user.getName());
    }
}
