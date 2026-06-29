package com.samteo.domain.authentication.service;

import com.samteo.domain.authentication.dto.request.LoginRequest;
import com.samteo.domain.authentication.dto.request.OAuthLinkRequest;
import com.samteo.domain.authentication.dto.request.RegisterRequest;
import com.samteo.domain.authentication.dto.response.AuthResponse;
import com.samteo.domain.authentication.dto.response.OAuthLoginResult;
import com.samteo.domain.authentication.entity.OAuthPendingIdentity;
import com.samteo.domain.authentication.entity.UserAuthIdentity;
import com.samteo.domain.authentication.repository.OAuthPendingIdentityRepository;
import com.samteo.domain.authentication.repository.UserAuthIdentityRepository;
import com.samteo.domain.user.entity.User;
import com.samteo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.UUID;

/**
 * 공통 사용자 도메인 모델을 기준으로 로컬 인증과 카카오 인증을 처리한다.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserAuthIdentityRepository userAuthIdentityRepository;
    private final OAuthPendingIdentityRepository oauthPendingIdentityRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

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
        userAuthIdentityRepository.save(UserAuthIdentity.create(
                user.getUserId(),
                "local",
                normalizeEmail(user.getEmail()),
                user.getEmail()
        ));
        return toAuthResponse(user);
    }

    /**
     * 이메일과 비밀번호를 검증하여 기존 로컬 계정을 인증한다.
     *
     * @param request 로그인 요청 정보
     * @return JWT가 포함된 인증 응답
     */
    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));
        if (user.getPasswordHash() == null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password.");
        }
        if (userAuthIdentityRepository.findByUserIdAndProvider(user.getUserId(), "local").isEmpty()) {
            userAuthIdentityRepository.save(UserAuthIdentity.create(
                    user.getUserId(),
                    "local",
                    normalizeEmail(user.getEmail()),
                    user.getEmail()
            ));
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
    public OAuthLoginResult loginOrRegisterOAuth(String provider, String providerId, String email, String name) {
        String normalizedProvider = normalizeProvider(provider);
        String normalizedEmail = normalizeEmail(email);

        UserAuthIdentity identity = userAuthIdentityRepository
                .findByProviderAndProviderUserId(normalizedProvider, providerId)
                .orElse(null);
        if (identity != null) {
            User user = userRepository.findById(identity.getUserId())
                    .orElseThrow(() -> new IllegalStateException("Connected user account was not found."));
            identity.recordLogin(email);
            return OAuthLoginResult.authenticated(toAuthResponse(user));
        }

        // Compatibility path for users created before user_auth_identities existed.
        User legacyUser = userRepository.findByProviderAndProviderId(normalizedProvider, providerId).orElse(null);
        if (legacyUser != null) {
            userAuthIdentityRepository.save(UserAuthIdentity.create(
                    legacyUser.getUserId(), normalizedProvider, providerId, email
            ));
            return OAuthLoginResult.authenticated(toAuthResponse(legacyUser));
        }

        if (userRepository.findByEmailIgnoreCase(normalizedEmail).isPresent()) {
            return OAuthLoginResult.linkRequired(
                    createPendingIdentity(normalizedProvider, providerId, email, name),
                    normalizedProvider,
                    email
            );
        }

        User user = userRepository.save(User.createOAuth(email, name, normalizedProvider, providerId));
        userAuthIdentityRepository.save(UserAuthIdentity.create(
                user.getUserId(), normalizedProvider, providerId, email
        ));
        return OAuthLoginResult.authenticated(toAuthResponse(user));
    }

    @Transactional
    public AuthResponse linkOAuthIdentity(Long userId, OAuthLinkRequest request) {
        if (request == null || request.getLinkToken() == null || request.getLinkToken().isBlank()) {
            throw new IllegalArgumentException("Account link token is required.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        OAuthPendingIdentity pending = oauthPendingIdentityRepository
                .findByPendingToken(request.getLinkToken())
                .orElseThrow(() -> new IllegalArgumentException("Account link request was not found."));

        if (!pending.isAvailable(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Account link request has expired.");
        }

        userAuthIdentityRepository
                .findByProviderAndProviderUserId(pending.getProvider(), pending.getProviderUserId())
                .ifPresent(identity -> {
                    if (!identity.getUserId().equals(userId)) {
                        throw new IllegalArgumentException("This login method is connected to another account.");
                    }
                });

        UserAuthIdentity currentProvider = userAuthIdentityRepository
                .findByUserIdAndProvider(userId, pending.getProvider())
                .orElse(null);
        if (currentProvider != null
                && !currentProvider.getProviderUserId().equals(pending.getProviderUserId())) {
            throw new IllegalArgumentException("A different account from this provider is already connected.");
        }
        if (currentProvider == null) {
            userAuthIdentityRepository.save(UserAuthIdentity.create(
                    userId,
                    pending.getProvider(),
                    pending.getProviderUserId(),
                    pending.getProviderEmail()
            ));
        } else {
            currentProvider.recordLogin(pending.getProviderEmail());
        }

        pending.consume();
        return toAuthResponse(user);
    }

    private String createPendingIdentity(String provider, String providerId, String email, String name) {
        OAuthPendingIdentity pending = OAuthPendingIdentity.create(
                UUID.randomUUID().toString(),
                provider,
                providerId,
                email,
                name,
                OffsetDateTime.now().plusMinutes(10)
        );
        return oauthPendingIdentityRepository.save(pending).getPendingToken();
    }

    private String normalizeProvider(String provider) {
        return provider == null ? "" : provider.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private AuthResponse toAuthResponse(User user) {
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getUserId(), user.getEmail(), user.getName(), user.getRole());
    }
}
