package com.samteo.domain.user.service;

import com.samteo.domain.user.dto.request.UserProfileUpdateRequest;
import com.samteo.domain.user.dto.response.UserResponse;
import com.samteo.domain.user.entity.User;
import com.samteo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 공통 사용자 저장소를 기반으로 사용자 조회 기능을 제공한다.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 현재 인증된 사용자의 프로필 정보를 조회한다.
     *
     * @param userId 보안 컨텍스트에서 전달된 인증 사용자 ID
     * @return 현재 사용자 프로필 응답
     */
    @Transactional(readOnly = true)
    public UserResponse getMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateMe(Long userId, UserProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        String email = request.getEmail() == null ? "" : request.getEmail().trim();
        String name = request.getName() == null ? "" : request.getName().trim();

        if (email.isBlank()) {
            throw new IllegalArgumentException("이메일을 입력해 주세요.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("회원명을 입력해 주세요.");
        }

        userRepository.findByEmailIgnoreCase(email)
                .filter(existing -> !existing.getUserId().equals(userId))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
                });

        user.updateProfile(email, name);
        return UserResponse.from(user);
    }
}
