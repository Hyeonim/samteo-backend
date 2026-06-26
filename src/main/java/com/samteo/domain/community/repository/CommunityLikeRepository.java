package com.samteo.domain.community.repository;

import com.samteo.domain.community.entity.CommunityLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {

    boolean existsByPostPostIdAndUserUserId(Long postId, Long userId);

    Optional<CommunityLike> findByPostPostIdAndUserUserId(Long postId, Long userId);
}
