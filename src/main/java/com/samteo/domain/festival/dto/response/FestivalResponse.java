package com.samteo.domain.festival.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FestivalResponse {

    private String title;
    private String startDate;
    private String endDate;
    private String location;
}
