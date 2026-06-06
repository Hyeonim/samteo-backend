package com.samteo.domain.festival.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * API 클라이언트에 반환할 표준화된 축제 정보 응답 데이터를 표현한다.
 */
@Getter
@Builder
public class FestivalResponse {

    private String title;
    private String startDate;
    private String endDate;
    private String location;
}
