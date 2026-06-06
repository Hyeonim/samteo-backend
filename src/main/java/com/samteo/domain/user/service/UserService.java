package com.samteo.domain.user.service;

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
}
