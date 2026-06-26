package com.samteo.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CommunityPostResponse {

    private Long id;
    private Long authorId;
    private String authorName;
    private String content;
    private List<CommunityImageResponse> images;
    private int likeCount;
    private int commentCount;
    private boolean liked;
    private String createdAt;
    private String updatedAt;
}
