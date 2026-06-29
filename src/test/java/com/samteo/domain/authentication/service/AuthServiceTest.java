package com.samteo.domain.authentication.service;

import com.samteo.domain.authentication.dto.request.OAuthLinkRequest;
import com.samteo.domain.authentication.dto.response.AuthResponse;
import com.samteo.domain.authentication.dto.response.OAuthLoginResult;
import com.samteo.domain.authentication.entity.OAuthPendingIdentity;
import com.samteo.domain.authentication.entity.UserAuthIdentity;
import com.samteo.domain.authentication.repository.OAuthPendingIdentityRepository;
import com.samteo.domain.authentication.repository.UserAuthIdentityRepository;
import com.samteo.domain.user.entity.User;
import com.samteo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAuthIdentityRepository userAuthIdentityRepository;

    @Mock
    private OAuthPendingIdentityRepository oauthPendingIdentityRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void requestsExplicitLinkWhenAnotherProviderUsesAnExistingEmail() {
        User existingUser = user(10L, "abc@naver.com", "kakao", "kakao-user-id");
        when(userAuthIdentityRepository.findByProviderAndProviderUserId("naver", "naver-user-id"))
                .thenReturn(Optional.empty());
        when(userRepository.findByProviderAndProviderId("naver", "naver-user-id"))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmailIgnoreCase("abc@naver.com"))
                .thenReturn(Optional.of(existingUser));
        when(oauthPendingIdentityRepository.save(any(OAuthPendingIdentity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OAuthLoginResult result = authService.loginOrRegisterOAuth(
                "naver", "naver-user-id", "abc@naver.com", "Test User"
        );

        assertThat(result.isLinkRequired()).isTrue();
        assertThat(result.getProvider()).isEqualTo("naver");
        assertThat(result.getLinkToken()).isNotBlank();
        assertThat(existingUser.getProvider()).isEqualTo("kakao");
        assertThat(existingUser.getProviderId()).isEqualTo("kakao-user-id");
        verify(userAuthIdentityRepository, never()).save(any(UserAuthIdentity.class));
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    void logsInThroughAnAlreadyConnectedIdentity() {
        User user = user(20L, "abc@example.com", "kakao", "kakao-user-id");
        UserAuthIdentity identity = UserAuthIdentity.create(
                20L, "naver", "naver-user-id", "abc@example.com"
        );
        when(userAuthIdentityRepository.findByProviderAndProviderUserId("naver", "naver-user-id"))
                .thenReturn(Optional.of(identity));
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        OAuthLoginResult result = authService.loginOrRegisterOAuth(
                "naver", "naver-user-id", "abc@example.com", "Test User"
        );

        assertThat(result.isLinkRequired()).isFalse();
        assertThat(result.getAuth().getToken()).isEqualTo("jwt-token");
        assertThat(identity.getLastLoginAt()).isNotNull();
    }

    @Test
    void linksPendingIdentityAfterExistingAccountAuthentication() {
        User user = user(30L, "abc@example.com", "kakao", "kakao-user-id");
        OAuthPendingIdentity pending = OAuthPendingIdentity.create(
                "pending-token",
                "naver",
                "naver-user-id",
                "abc@example.com",
                "Test User",
                OffsetDateTime.now().plusMinutes(5)
        );
        when(userRepository.findById(30L)).thenReturn(Optional.of(user));
        when(oauthPendingIdentityRepository.findByPendingToken("pending-token"))
                .thenReturn(Optional.of(pending));
        when(userAuthIdentityRepository.findByProviderAndProviderUserId("naver", "naver-user-id"))
                .thenReturn(Optional.empty());
        when(userAuthIdentityRepository.findByUserIdAndProvider(30L, "naver"))
                .thenReturn(Optional.empty());
        when(jwtService.generateToken(user)).thenReturn("linked-jwt");

        AuthResponse response = authService.linkOAuthIdentity(30L, new OAuthLinkRequest("pending-token"));

        assertThat(response.getToken()).isEqualTo("linked-jwt");
        assertThat(pending.getConsumedAt()).isNotNull();
        verify(userAuthIdentityRepository).save(any(UserAuthIdentity.class));
    }

    private User user(Long userId, String email, String provider, String providerId) {
        User user = User.createOAuth(email, "Test User", provider, providerId);
        ReflectionTestUtils.setField(user, "userId", userId);
        return user;
    }
}
