package com.samteo.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommunityImageResponse {

    private Long id;
    private String url;
    private int sortOrder;
}
