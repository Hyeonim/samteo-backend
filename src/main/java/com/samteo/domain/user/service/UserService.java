package com.samteo.domain.user.service;

import com.samteo.domain.user.dto.request.UserProfileUpdateRequest;
import com.samteo.domain.user.dto.response.UserProfileResponse;
import com.samteo.domain.user.dto.response.UserResponse;
import com.samteo.domain.community.repository.CommunityPostRepository;
import com.samteo.domain.authentication.repository.UserAuthIdentityRepository;
import com.samteo.domain.user.entity.User;
import com.samteo.domain.user.entity.UserFollow;
import com.samteo.domain.user.repository.UserFollowRepository;
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
    private final UserAuthIdentityRepository userAuthIdentityRepository;
    private final UserFollowRepository userFollowRepository;
    private final CommunityPostRepository communityPostRepository;

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
        return toUserResponse(user);
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
        return toUserResponse(user);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(Long profileUserId, Long viewerId) {
        User user = findUser(profileUserId);
        return toProfileResponse(user, viewerId);
    }

    @Transactional
    public UserProfileResponse follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("본인은 팔로우할 수 없습니다.");
        }

        User follower = findUser(followerId);
        User following = findUser(followingId);
        if (!userFollowRepository.existsByFollowerUserIdAndFollowingUserId(followerId, followingId)) {
            userFollowRepository.save(UserFollow.create(follower, following));
        }
        return toProfileResponse(following, followerId);
    }

    @Transactional
    public UserProfileResponse unfollow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("본인은 언팔로우할 수 없습니다.");
        }

        User following = findUser(followingId);
        userFollowRepository.findByFollowerUserIdAndFollowingUserId(followerId, followingId)
                .ifPresent(userFollowRepository::delete);
        return toProfileResponse(following, followerId);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    private UserResponse toUserResponse(User user) {
        java.util.List<String> providers = userAuthIdentityRepository
                .findAllByUserIdOrderByLinkedAtAsc(user.getUserId())
                        .stream()
                        .map(identity -> identity.getProvider())
                        .toList();
        return UserResponse.from(user, providers.isEmpty() ? java.util.List.of(user.getProvider()) : providers);
    }

    private UserProfileResponse toProfileResponse(User user, Long viewerId) {
        Long profileUserId = user.getUserId();
        boolean me = viewerId != null && viewerId.equals(profileUserId);
        boolean followedByMe = viewerId != null
                && !me
                && userFollowRepository.existsByFollowerUserIdAndFollowingUserId(viewerId, profileUserId);
        return UserProfileResponse.of(
                user,
                communityPostRepository.countByUserUserIdAndDeletedAtIsNull(profileUserId),
                userFollowRepository.countByFollowingUserId(profileUserId),
                userFollowRepository.countByFollowerUserId(profileUserId),
                me,
                followedByMe
        );
    }
}
