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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmailIgnoreCase(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use.");
        }

        String hash = passwordEncoder.encode(request.getPassword());
        User user = userRepository.save(User.createLocal(request.getEmail(), request.getName(), hash));
        return toAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));
        if (user.getPasswordHash() == null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password.");
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
