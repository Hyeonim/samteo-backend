package com.samteo.domain.user.service;

import com.samteo.domain.user.dto.response.UserResponse;
import com.samteo.domain.user.entity.User;
import com.samteo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        return UserResponse.from(user);
    }
}
