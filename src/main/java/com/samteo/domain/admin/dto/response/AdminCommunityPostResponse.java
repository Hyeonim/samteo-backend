package com.samteo.domain.admin.dto.response;

import com.samteo.domain.community.entity.CommunityPost;
import com.samteo.domain.community.entity.CommunityPostImage;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Comparator;

@Getter
@Builder
public class AdminCommunityPostResponse {

    private Long postId;
    private Long authorId;
    private String authorName;
    private String authorEmail;
    private String content;
    private String thumbnailUrl;
    private int imageCount;
    private int likeCount;
    private int commentCount;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static AdminCommunityPostResponse from(CommunityPost post) {
        String thumbnailUrl = post.getImages().stream()
                .min(Comparator.comparingInt(CommunityPostImage::getSortOrder))
                .map(CommunityPostImage::getImageUrl)
                .orElse(null);

        return AdminCommunityPostResponse.builder()
                .postId(post.getPostId())
                .authorId(post.getUser().getUserId())
                .authorName(post.getUser().getName())
                .authorEmail(post.getUser().getEmail())
                .content(post.getContent())
                .thumbnailUrl(thumbnailUrl)
                .imageCount(post.getImages().size())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
