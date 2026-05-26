package com.samteo.domain.user.service;

import com.samteo.domain.user.dto.request.LoginRequest;
import com.samteo.domain.user.dto.response.LoginResponse;
import com.samteo.domain.user.dto.response.UserResponse;
import com.samteo.domain.user.entity.User;
import com.samteo.domain.user.repository.DummyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final long DUMMY_EXPIRES_IN = 3600L;

    private final DummyUserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        String email = request.getEmail() == null || request.getEmail().isBlank()
                ? "demo@samteo.local"
                : request.getEmail();
        String nickname = request.getNickname() == null || request.getNickname().isBlank()
                ? "Samteo Demo"
                : request.getNickname();
        String provider = request.getProvider() == null || request.getProvider().isBlank()
                ? "KAKAO"
                : request.getProvider().toUpperCase();

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .nickname(nickname)
                        .provider(provider)
                        .providerId("dummy-" + email)
                        .profileImageUrl(null)
                        .role("USER")
                        .build()));

        return LoginResponse.builder()
                .tokenType("Bearer")
                .accessToken(createDummyToken(user))
                .expiresIn(DUMMY_EXPIRES_IN)
                .user(UserResponse.from(user))
                .build();
    }

    public UserResponse getMe() {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        return UserResponse.from(user);
    }

    private String createDummyToken(User user) {
        String payload = user.getId() + ":" + user.getEmail() + ":dummy";
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(payload.getBytes(StandardCharsets.UTF_8));
    }
}
