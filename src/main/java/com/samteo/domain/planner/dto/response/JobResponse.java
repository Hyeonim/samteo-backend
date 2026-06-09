package com.samteo.domain.planner.dto.response;

import com.samteo.domain.planner.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class JobResponse {

    private String id;
    private String name;
    private String title;
    private String company;
    private String cityId;
    private String cityName;
    private String regionId;
    private String district;
    private String region;
    private String address;
    private String type;
    private String category;
    private String employmentType;
    private int monthlySalary;
    private int salary;
    private String workingDays;
    private int commuteMinutes;
    private String desc;
    private String location;
    private String priceLabel;
    private String unit;
    private String sub;
    private String emoji;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal lat;
    private BigDecimal lng;
    private List<String> tags;
    private String source;

    public static JobResponse from(Job job) {
        List<String> tags = job.getTags() == null ? List.of() : new ArrayList<>(job.getTags());
        String category = toKoreanCategory(job.getCategory());
        String employmentType = toKoreanEmploymentType(job.getEmploymentType());
        return JobResponse.builder()
                .id(job.getId())
                .name(job.getTitle())
                .title(job.getTitle())
                .company(job.getCompany())
                .cityId(job.getCityId())
                .cityName(job.getCityName())
                .regionId(job.getRegionId())
                .district(job.getDistrict())
                .region(job.getCityName() == null ? job.getDistrict() : job.getCityName() + " " + job.getDistrict())
                .address(job.getAddress())
                .type(category)
                .category(category)
                .employmentType(employmentType)
                .monthlySalary(job.getMonthlySalary())
                .salary(job.getMonthlySalary())
                .workingDays(toKoreanWorkingDays(job.getWorkingDays()))
                .commuteMinutes(job.getCommuteMinutes())
                .desc(job.getCompany() + " / " + toKoreanWorkingDays(job.getWorkingDays()))
                .location(job.getAddress() == null ? job.getDistrict() + " / commute " + job.getCommuteMinutes() + " min" : job.getAddress())
                .priceLabel(job.getMonthlySalary() == 0 ? "-" : job.getMonthlySalary() + "원")
                .unit("/월")
                .sub("샘플 일자리 데이터")
                .emoji(toJobEmoji(job.getCategory()))
                .latitude(job.getLatitude())
                .longitude(job.getLongitude())
                .lat(job.getLatitude())
                .lng(job.getLongitude())
                .tags(tags.stream().map(JobResponse::toKoreanTag).toList())
                .source("HR_DUMMY")
                .build();
    }

    private static String toKoreanCategory(String category) {
        if (category == null) return "추천";
        return switch (category) {
            case "Food and Beverage" -> "카페·식음료";
            case "Accommodation" -> "숙박";
            case "Tourism" -> "관광";
            case "Store" -> "매장";
            case "Food Service" -> "외식";
            case "Logistics" -> "물류";
            case "Event" -> "행사";
            case "Mobility" -> "이동서비스";
            case "Shopping" -> "판매";
            default -> category;
        };
    }

    private static String toKoreanEmploymentType(String employmentType) {
        if (employmentType == null) return "-";
        return switch (employmentType) {
            case "Part-time" -> "파트타임";
            case "Short contract" -> "단기 계약";
            case "Contract" -> "계약직";
            default -> employmentType;
        };
    }

    private static String toKoreanWorkingDays(String workingDays) {
        if (workingDays == null) return "-";
        return switch (workingDays) {
            case "Mon-Fri 10:00-17:00" -> "월-금 10:00-17:00";
            case "Tue-Sat 09:00-18:00" -> "화-토 09:00-18:00";
            case "Wed-Sun 09:00-18:00" -> "수-일 09:00-18:00";
            case "3 nights per week" -> "주 3회 야간";
            case "Thu-Mon 11:00-20:00" -> "목-월 11:00-20:00";
            case "Mon-Fri 08:00-17:00" -> "월-금 08:00-17:00";
            case "Fri-Sun 12:00-21:00" -> "금-일 12:00-21:00";
            case "Mon-Sat 09:00-18:00" -> "월-토 09:00-18:00";
            case "Tue-Sun 10:00-19:00" -> "화-일 10:00-19:00";
            case "5 mornings per week" -> "주 5회 오전";
            default -> workingDays;
        };
    }

    private static String toKoreanTag(String tag) {
        if (tag == null) return "";
        return switch (tag) {
            case "cafe" -> "카페";
            case "beginner" -> "초보 가능";
            case "day-shift" -> "주간";
            case "stay" -> "숙박업";
            case "cleaning" -> "객실관리";
            case "guest-service" -> "고객응대";
            case "tourism" -> "관광";
            case "language-plus" -> "외국어 우대";
            case "weekend" -> "주말";
            case "night" -> "야간";
            case "store" -> "매장";
            case "short-term" -> "단기";
            case "meal-support" -> "식사 제공";
            case "service" -> "서비스";
            case "local-food" -> "로컬푸드";
            case "logistics" -> "물류";
            case "standing" -> "입식근무";
            case "event" -> "행사";
            case "team-work" -> "팀근무";
            case "desk" -> "데스크";
            case "driver-license-plus" -> "운전면허 우대";
            case "sales" -> "판매";
            case "morning" -> "오전";
            case "hotel" -> "호텔";
            default -> tag;
        };
    }

    private static String toJobEmoji(String category) {
        if (category == null) return "💼";
        return switch (category) {
            case "Food and Beverage" -> "☕";
            case "Accommodation" -> "🏠";
            case "Tourism" -> "🧭";
            case "Store" -> "🏪";
            case "Food Service" -> "🍽";
            case "Logistics" -> "📦";
            case "Event" -> "🎪";
            case "Mobility" -> "🚗";
            case "Shopping" -> "🛍";
            default -> "💼";
        };
    }
}
