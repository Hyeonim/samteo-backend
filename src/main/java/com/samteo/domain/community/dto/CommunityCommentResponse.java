package com.samteo.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommunityCommentResponse {

    private Long id;
    private Long authorId;
    private String authorName;
    private String content;
    private String createdAt;
}
