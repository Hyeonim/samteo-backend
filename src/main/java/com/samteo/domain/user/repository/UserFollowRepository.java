package com.samteo.domain.user.repository;

import com.samteo.domain.user.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {

    boolean existsByFollowerUserIdAndFollowingUserId(Long followerId, Long followingId);

    Optional<UserFollow> findByFollowerUserIdAndFollowingUserId(Long followerId, Long followingId);

    long countByFollowerUserId(Long followerId);

    long countByFollowingUserId(Long followingId);
}
