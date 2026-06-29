package com.samteo.domain.authentication.service;

import com.samteo.domain.authentication.dto.response.AuthResponse;
import com.samteo.domain.user.entity.User;
import com.samteo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void updatesProviderWhenSameEmailLogsInThroughAnotherOAuthProvider() {
        User existingUser = User.createOAuth("abc@naver.com", "테스트", "kakao", "kakao-user-id");
        when(userRepository.findByProviderAndProviderId("naver", "naver-user-id"))
                .thenReturn(Optional.empty());
        when(userRepository.findByEmailIgnoreCase("abc@naver.com"))
                .thenReturn(Optional.of(existingUser));
        when(jwtService.generateToken(existingUser)).thenReturn("jwt-token");

        AuthResponse response = authService.loginOrRegisterOAuth(
                "naver",
                "naver-user-id",
                "abc@naver.com",
                "테스트"
        );

        assertThat(existingUser.getProvider()).isEqualTo("naver");
        assertThat(existingUser.getProviderId()).isEqualTo("naver-user-id");
        assertThat(response.getToken()).isEqualTo("jwt-token");
        verify(userRepository, never()).save(existingUser);
    }
}
