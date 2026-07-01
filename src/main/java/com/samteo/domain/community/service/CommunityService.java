package com.samteo.domain.community.service;

import com.samteo.domain.community.dto.CommunityCommentPageResponse;
import com.samteo.domain.community.dto.CommunityCommentResponse;
import com.samteo.domain.community.dto.CommunityImageResponse;
import com.samteo.domain.community.dto.CommunityPostPageResponse;
import com.samteo.domain.community.dto.CommunityPostResponse;
import com.samteo.domain.community.entity.CommunityComment;
import com.samteo.domain.community.entity.CommunityLike;
import com.samteo.domain.community.entity.CommunityPost;
import com.samteo.domain.community.entity.CommunityPostImage;
import com.samteo.domain.community.repository.CommunityCommentRepository;
import com.samteo.domain.community.repository.CommunityLikeRepository;
import com.samteo.domain.community.repository.CommunityPostRepository;
import com.samteo.domain.notification.service.NotificationService;
import com.samteo.domain.user.entity.User;
import com.samteo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityPostRepository postRepository;
    private final CommunityCommentRepository commentRepository;
    private final CommunityLikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CommunityImageStorageService imageStorageService;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public CommunityPostPageResponse getPosts(Long viewerId, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), normalizeSize(size));
        Page<CommunityPost> posts = postRepository.findByDeletedAtIsNullOrderByCreatedAtDesc(pageable);
        return new CommunityPostPageResponse(
                posts.getContent().stream().map(post -> toPostResponse(post, viewerId)).toList(),
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.hasNext()
        );
    }

    @Transactional(readOnly = true)
    public CommunityPostResponse getPost(Long postId, Long viewerId) {
        return toPostResponse(findPost(postId), viewerId);
    }

    @Transactional(readOnly = true)
    public CommunityPostPageResponse getMyPosts(Long userId, int page, int size) {
        return getPostsByUser(userId, userId, page, size);
    }

    @Transactional(readOnly = true)
    public CommunityPostPageResponse getPostsByUser(Long profileUserId, Long viewerId, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), normalizeSize(size));
        Page<CommunityPost> posts =
                postRepository.findByUserUserIdAndDeletedAtIsNullOrderByCreatedAtDesc(profileUserId, pageable);
        return new CommunityPostPageResponse(
                posts.getContent().stream().map(post -> toPostResponse(post, viewerId)).toList(),
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.hasNext()
        );
    }

    @Transactional(readOnly = true)
    public CommunityPostPageResponse getPostsByTag(String tag, Long viewerId, int page, int size) {
        String normalizedTag = normalizeTag(tag);
        Pageable pageable = PageRequest.of(Math.max(page, 0), normalizeSize(size));
        Page<CommunityPost> posts = postRepository.findByHashtag(normalizedTag, pageable);
        return new CommunityPostPageResponse(
                posts.getContent().stream().map(post -> toPostResponse(post, viewerId)).toList(),
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.hasNext()
        );
    }

    @Transactional
    public CommunityPostResponse createPost(Long userId, String content, List<MultipartFile> images) {
        String normalizedContent = content == null ? "" : content.trim();
        List<MultipartFile> validImages = images == null
                ? List.of()
                : images.stream().filter(file -> file != null && !file.isEmpty()).toList();

        if (normalizedContent.isBlank() && validImages.isEmpty()) {
            throw new IllegalArgumentException("내용 또는 이미지를 입력해 주세요.");
        }

        User user = findUser(userId);
        CommunityPost post = postRepository.saveAndFlush(CommunityPost.create(user, normalizedContent));
        for (int i = 0; i < validImages.size(); i++) {
            post.addImage(imageStorageService.storeCommunityImage(validImages.get(i), post.getPostId()), i);
        }
        notificationService.notifyPostCreated(userId, post.getPostId());
        return toPostResponse(post, userId);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        CommunityPost post = findPost(postId);
        if (!post.isOwnedBy(userId)) {
            throw new IllegalArgumentException("본인이 작성한 게시글만 삭제할 수 있습니다.");
        }
        post.softDelete();
    }

    @Transactional
    public CommunityPostResponse updatePost(Long userId, Long postId, String content) {
        CommunityPost post = findPost(postId);
        if (!post.isOwnedBy(userId)) {
            throw new IllegalArgumentException("본인이 작성한 게시글만 수정할 수 있습니다.");
        }

        String normalizedContent = content == null ? "" : content.trim();
        if (normalizedContent.isBlank() && !post.hasImages()) {
            throw new IllegalArgumentException("내용 또는 이미지가 필요합니다.");
        }

        post.updateContent(normalizedContent);
        return toPostResponse(post, userId);
    }

    @Transactional
    public CommunityPostResponse likePost(Long userId, Long postId) {
        CommunityPost post = findPost(postId);
        if (!likeRepository.existsByPostPostIdAndUserUserId(postId, userId)) {
            User actor = findUser(userId);
            likeRepository.save(CommunityLike.create(post, actor));
            post.increaseLikeCount();
            notificationService.notifyPostLiked(
                    post.getUser().getUserId(),
                    actor.getUserId(),
                    actor.getName(),
                    postId
            );
        }
        return toPostResponse(post, userId);
    }

    @Transactional
    public CommunityPostResponse unlikePost(Long userId, Long postId) {
        CommunityPost post = findPost(postId);
        likeRepository.findByPostPostIdAndUserUserId(postId, userId).ifPresent(like -> {
            likeRepository.delete(like);
            post.decreaseLikeCount();
        });
        return toPostResponse(post, userId);
    }

    @Transactional(readOnly = true)
    public CommunityCommentPageResponse getComments(Long postId, int page, int size) {
        findPost(postId);
        Pageable pageable = PageRequest.of(Math.max(page, 0), normalizeSize(size));
        Page<CommunityComment> comments =
                commentRepository.findByPostPostIdAndDeletedAtIsNullOrderByCreatedAtAsc(postId, pageable);
        return new CommunityCommentPageResponse(
                comments.getContent().stream().map(this::toCommentResponse).toList(),
                comments.getNumber(),
                comments.getSize(),
                comments.getTotalElements(),
                comments.getTotalPages(),
                comments.hasNext()
        );
    }

    @Transactional
    public CommunityCommentResponse createComment(Long userId, Long postId, String content) {
        if (content == null || content.trim().isBlank()) {
            throw new IllegalArgumentException("댓글 내용을 입력해 주세요.");
        }
        CommunityPost post = findPost(postId);
        User actor = findUser(userId);
        CommunityComment comment = commentRepository.save(
                CommunityComment.create(post, actor, content.trim())
        );
        post.increaseCommentCount();
        notificationService.notifyPostCommented(
                post.getUser().getUserId(),
                actor.getUserId(),
                actor.getName(),
                postId,
                comment.getCommentId()
        );
        return toCommentResponse(comment);
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        CommunityComment comment = commentRepository.findByCommentIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        if (!comment.isOwnedBy(userId)) {
            throw new IllegalArgumentException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }
        comment.softDelete();
        comment.getPost().decreaseCommentCount();
    }

    private CommunityPost findPost(Long postId) {
        return postRepository.findByPostIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private CommunityPostResponse toPostResponse(CommunityPost post, Long viewerId) {
        List<CommunityImageResponse> images = post.getImages().stream()
                .sorted(Comparator.comparingInt(CommunityPostImage::getSortOrder))
                .map(image -> new CommunityImageResponse(image.getImageId(), image.getImageUrl(), image.getSortOrder()))
                .toList();
        boolean liked = viewerId != null && likeRepository.existsByPostPostIdAndUserUserId(post.getPostId(), viewerId);
        return new CommunityPostResponse(
                post.getPostId(),
                post.getUser().getUserId(),
                post.getUser().getName(),
                post.getContent(),
                images,
                post.getLikeCount(),
                post.getCommentCount(),
                liked,
                post.getCreatedAt().toString(),
                post.getUpdatedAt().toString()
        );
    }

    private CommunityCommentResponse toCommentResponse(CommunityComment comment) {
        return new CommunityCommentResponse(
                comment.getCommentId(),
                comment.getUser().getUserId(),
                comment.getUser().getName(),
                comment.getContent(),
                comment.getCreatedAt().toString()
        );
    }

    private int normalizeSize(int size) {
        if (size <= 0) return 12;
        return Math.min(size, 50);
    }

    private String normalizeTag(String tag) {
        String normalized = tag == null ? "" : tag.trim().replaceFirst("^#", "");
        if (normalized.isBlank() || normalized.length() > 100
                || !normalized.matches("[\\p{L}\\p{N}_]+")) {
            throw new IllegalArgumentException("올바른 태그를 입력해 주세요.");
        }
        return normalized;
    }
}
