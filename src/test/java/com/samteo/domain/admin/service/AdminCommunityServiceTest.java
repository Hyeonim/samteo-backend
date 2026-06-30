package com.samteo.domain.admin.service;

import com.samteo.domain.admin.dto.response.AdminCommunityPostResponse;
import com.samteo.domain.community.entity.CommunityPost;
import com.samteo.domain.community.repository.CommunityPostRepository;
import com.samteo.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminCommunityServiceTest {

    @Mock
    private CommunityPostRepository communityPostRepository;

    @InjectMocks
    private AdminCommunityService adminCommunityService;

    @Test
    void updatesPostContentWithoutChangingOwnership() {
        CommunityPost post = post(10L, "before");
        when(communityPostRepository.findByPostIdAndDeletedAtIsNull(10L))
                .thenReturn(Optional.of(post));

        AdminCommunityPostResponse response = adminCommunityService.updatePost(10L, " after ");

        assertThat(post.getContent()).isEqualTo("after");
        assertThat(response.getContent()).isEqualTo("after");
        assertThat(response.getAuthorId()).isEqualTo(1L);
    }

    @Test
    void softDeletesPost() {
        CommunityPost post = post(20L, "content");
        when(communityPostRepository.findByPostIdAndDeletedAtIsNull(20L))
                .thenReturn(Optional.of(post));

        adminCommunityService.deletePost(20L);

        assertThat(post.getDeletedAt()).isNotNull();
    }

    private CommunityPost post(Long postId, String content) {
        User user = User.createLocal("admin@example.com", "Admin", "password");
        ReflectionTestUtils.setField(user, "userId", 1L);
        CommunityPost post = CommunityPost.create(user, content);
        ReflectionTestUtils.setField(post, "postId", postId);
        return post;
    }
}
