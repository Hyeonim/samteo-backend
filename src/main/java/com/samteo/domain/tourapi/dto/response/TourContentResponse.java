package com.samteo.domain.tourapi.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TourContentResponse {

    private Long contentId;
    private Integer contentTypeId;
    private String title;
    private String addr1;
    private String addr2;
    private Integer areaCode;
    private Integer sigunguCode;
    private Double mapx;
    private Double mapy;
    private String firstImage;
    private String firstImage2;
    private String tel;
    private String cat1;
    private String cat2;
    private String cat3;
    private String createdTime;
    private String modifiedTime;
    private String eventStartDate;
    private String eventEndDate;
}
