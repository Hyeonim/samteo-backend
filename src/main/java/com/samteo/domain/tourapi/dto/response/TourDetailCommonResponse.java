package com.samteo.domain.tourapi.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TourDetailCommonResponse {

    private Long contentId;
    private Integer contentTypeId;
    private String title;
    private String tel;
    private String telName;
    private String homepage;
    private String addr1;
    private String addr2;
    private String zipcode;
    private Double mapx;
    private Double mapy;
    private String firstImage;
    private String firstImage2;
    private String overview;
    private String createdTime;
    private String modifiedTime;
}
