package com.samteo.domain.community.repository;

import com.samteo.domain.community.entity.CommunityPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {

    @EntityGraph(attributePaths = {"user", "images"})
    Page<CommunityPost> findByDeletedAtIsNullOrderByCreatedAtDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "images"})
    Page<CommunityPost> findByUserUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query(
            value = """
                    SELECT post.*
                    FROM community_posts post
                    WHERE post.deleted_at IS NULL
                      AND post.content REGEXP CONCAT('(^|[[:space:]])#', :tag, '([[:space:]]|$)')
                    ORDER BY post.created_at DESC
                    """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM community_posts post
                    WHERE post.deleted_at IS NULL
                      AND post.content REGEXP CONCAT('(^|[[:space:]])#', :tag, '([[:space:]]|$)')
                    """,
            nativeQuery = true
    )
    Page<CommunityPost> findByHashtag(@Param("tag") String tag, Pageable pageable);

    long countByUserUserIdAndDeletedAtIsNull(Long userId);

    @EntityGraph(attributePaths = {"user", "images"})
    Optional<CommunityPost> findByPostIdAndDeletedAtIsNull(Long postId);

    @EntityGraph(attributePaths = {"user"})
    @Query("""
            SELECT post
            FROM CommunityPost post
            WHERE post.deletedAt IS NULL
              AND (
                :keyword = ''
                OR LOWER(post.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(post.user.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(post.user.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
            ORDER BY post.createdAt DESC
            """)
    Page<CommunityPost> searchForAdmin(@Param("keyword") String keyword, Pageable pageable);
}
