package com.samteo.domain.admin.service;

import com.samteo.domain.admin.dto.response.AdminCommunityPostResponse;
import com.samteo.domain.community.entity.CommunityPost;
import com.samteo.domain.community.repository.CommunityPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminCommunityService {

    private final CommunityPostRepository communityPostRepository;

    @Transactional(readOnly = true)
    public Page<AdminCommunityPostResponse> getPosts(int page, int size, String keyword) {
        int normalizedPage = Math.max(page, 0);
        int normalizedSize = Math.min(Math.max(size, 1), 50);
        String normalizedKeyword = keyword == null ? "" : keyword.trim();
        return communityPostRepository
                .searchForAdmin(normalizedKeyword, PageRequest.of(normalizedPage, normalizedSize))
                .map(AdminCommunityPostResponse::from);
    }

    @Transactional
    public AdminCommunityPostResponse updatePost(Long postId, String content) {
        CommunityPost post = findPost(postId);
        String normalizedContent = content == null ? "" : content.trim();
        if (normalizedContent.isBlank() && !post.hasImages()) {
            throw new IllegalArgumentException("Content or an image is required.");
        }
        post.updateContent(normalizedContent);
        return AdminCommunityPostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long postId) {
        findPost(postId).softDelete();
    }

    private CommunityPost findPost(Long postId) {
        return communityPostRepository.findByPostIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new IllegalArgumentException("Community post not found: " + postId));
    }
}
