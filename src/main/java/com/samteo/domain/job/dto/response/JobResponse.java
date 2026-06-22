package com.samteo.domain.job.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * API 클라이언트에 반환할 표준화된 채용 공고 응답 데이터를 표현한다.
 */
@Getter
@Builder
public class JobResponse {

    private String id;
    private String type;
    private String title;
    private String company;
    private String location;
    private Integer wage;
    private Integer hourlyWage;
    private Integer monthlySalary;
    private String employmentType;
    private String education;
    private String description;
    private String startDate;
    private String endDate;
    private String sourceUrl;
}
