package com.samteo.domain.community.controller;

import com.samteo.domain.community.dto.CommunityCommentPageResponse;
import com.samteo.domain.community.dto.CommunityCommentRequest;
import com.samteo.domain.community.dto.CommunityCommentResponse;
import com.samteo.domain.community.dto.CommunityPostPageResponse;
import com.samteo.domain.community.dto.CommunityPostResponse;
import com.samteo.domain.community.dto.CommunityPostUpdateRequest;
import com.samteo.domain.community.service.CommunityService;
import com.samteo.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/community/posts")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping
    public ResponseEntity<ApiResponse<CommunityPostPageResponse>> getPosts(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(communityService.getPosts(userId, page, size)));
    }

    @GetMapping("/users/{profileUserId}")
    public ResponseEntity<ApiResponse<CommunityPostPageResponse>> getPostsByUser(
            @AuthenticationPrincipal Long viewerId,
            @PathVariable Long profileUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(communityService.getPostsByUser(profileUserId, viewerId, page, size)));
    }

    @GetMapping("/tags/{tag}")
    public ResponseEntity<ApiResponse<CommunityPostPageResponse>> getPostsByTag(
            @AuthenticationPrincipal Long viewerId,
            @PathVariable String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(communityService.getPostsByTag(tag, viewerId, page, size)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CommunityPostPageResponse>> getMyPosts(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(communityService.getMyPosts(userId, page, size)));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<CommunityPostResponse>> getPost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(ApiResponse.success(communityService.getPost(postId, userId)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<CommunityPostResponse>> createPost(
            @AuthenticationPrincipal Long userId,
            @RequestParam(value = "content", required = false) String content,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(communityService.createPost(userId, content, images)));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId
    ) {
        communityService.deletePost(userId, postId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{postId}")
    public ResponseEntity<ApiResponse<CommunityPostResponse>> updatePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId,
            @RequestBody CommunityPostUpdateRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(communityService.updatePost(userId, postId, request.getContent())));
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse<CommunityPostResponse>> likePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(ApiResponse.success(communityService.likePost(userId, postId)));
    }

    @DeleteMapping("/{postId}/likes")
    public ResponseEntity<ApiResponse<CommunityPostResponse>> unlikePost(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(ApiResponse.success(communityService.unlikePost(userId, postId)));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<CommunityCommentPageResponse>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(ApiResponse.success(communityService.getComments(postId, page, size)));
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<CommunityCommentResponse>> createComment(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId,
            @RequestBody CommunityCommentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(communityService.createComment(userId, postId, request.getContent())));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {
        communityService.deleteComment(userId, commentId);
        return ResponseEntity.noContent().build();
    }
}
