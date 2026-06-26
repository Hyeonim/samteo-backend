package com.samteo.domain.community.repository;

import com.samteo.domain.community.entity.CommunityComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {

    @EntityGraph(attributePaths = {"user"})
    Page<CommunityComment> findByPostPostIdAndDeletedAtIsNullOrderByCreatedAtAsc(Long postId, Pageable pageable);

    Optional<CommunityComment> findByCommentIdAndDeletedAtIsNull(Long commentId);
}
