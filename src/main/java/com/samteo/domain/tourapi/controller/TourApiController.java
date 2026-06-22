package com.samteo.domain.tourapi.controller;

import com.samteo.domain.tourapi.dto.response.TourContentResponse;
import com.samteo.domain.tourapi.dto.response.TourDetailCommonResponse;
import com.samteo.domain.tourapi.service.TourApiService;
import com.samteo.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tour")
@RequiredArgsConstructor
public class TourApiController {

    private final TourApiService tourApiService;

    /**
     * 지역기반 관광지 목록 (areaBasedList2)
     *
     * @param areaCode      지역코드 (선택, 예: 1=서울, 6=부산)
     * @param contentTypeId 콘텐츠 타입 (선택, 예: 12=관광지, 14=문화시설, 32=숙박, 39=음식점)
     * @param numOfRows     페이지당 결과 수 (기본 10)
     * @param pageNo        페이지 번호 (기본 1)
     */
    @GetMapping("/spots")
    public ResponseEntity<ApiResponse<List<TourContentResponse>>> getSpots(
            @RequestParam(required = false) Integer areaCode,
            @RequestParam(required = false) Integer contentTypeId,
            @RequestParam(defaultValue = "10") int numOfRows,
            @RequestParam(defaultValue = "1") int pageNo) {
        return ResponseEntity.ok(ApiResponse.success(
                tourApiService.getSpots(areaCode, contentTypeId, numOfRows, pageNo)));
    }

    /**
     * 축제/공연/행사 목록 (searchFestival2)
     *
     * @param eventStartDate 행사 시작일 이후 (YYYYMMDD, 기본 오늘)
     * @param areaCode       지역코드 (선택)
     * @param numOfRows      페이지당 결과 수 (기본 10)
     * @param pageNo         페이지 번호 (기본 1)
     */
    @GetMapping("/festivals")
    public ResponseEntity<ApiResponse<List<TourContentResponse>>> getFestivals(
            @RequestParam(required = false) String eventStartDate,
            @RequestParam(required = false) Integer areaCode,
            @RequestParam(defaultValue = "10") int numOfRows,
            @RequestParam(defaultValue = "1") int pageNo) {
        String startDate = (eventStartDate != null && !eventStartDate.isBlank())
                ? eventStartDate
                : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return ResponseEntity.ok(ApiResponse.success(
                tourApiService.getFestivals(startDate, areaCode, numOfRows, pageNo)));
    }

    /**
     * 공통 상세정보 (detailCommon2) — 제목, 주소, 이미지, 개요 등
     *
     * @param contentId 콘텐츠 ID
     */
    @GetMapping("/detail/common")
    public ResponseEntity<ApiResponse<TourDetailCommonResponse>> getDetailCommon(
            @RequestParam long contentId) {
        return ResponseEntity.ok(ApiResponse.success(
                tourApiService.getDetailCommon(contentId)));
    }

    /**
     * 소개 상세정보 (detailIntro2) — 콘텐츠 타입별 고유 필드 (운영시간, 입장료, 주차 등)
     *
     * @param contentId     콘텐츠 ID
     * @param contentTypeId 콘텐츠 타입 (12=관광지, 14=문화시설, 15=행사, 25=여행코스, 28=레포츠, 32=숙박, 38=쇼핑, 39=음식점)
     */
    @GetMapping("/detail/intro")
    public ResponseEntity<ApiResponse<Map<String, String>>> getDetailIntro(
            @RequestParam long contentId,
            @RequestParam int contentTypeId) {
        return ResponseEntity.ok(ApiResponse.success(
                tourApiService.getDetailIntro(contentId, contentTypeId)));
    }
}
