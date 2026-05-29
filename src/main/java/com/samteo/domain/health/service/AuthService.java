package com.samteo.domain.health.service;

import com.samteo.dto.request.LoginRequest;
import com.samteo.dto.request.RegisterRequest;
import com.samteo.global.response.AuthResponse;
import com.samteo.entity.User;
import com.samteo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.findByEmail(req.email()).isPresent()) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }
        String hash = passwordEncoder.encode(req.password());
        User user = userRepository.save(User.createLocal(req.email(), req.name(), hash));
        return toAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new RuntimeException("이메일 또는 비밀번호가 올바르지 않습니다."));
        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new RuntimeException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }
        return toAuthResponse(user);
    }

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
