package com.samteo.domain.job.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobResponse {

    private String id;
    private String type;
    private String title;
    private String company;
    private String location;
    private Integer wage;
    private String startDate;
    private String endDate;
}
