package com.samteo.domain.admin.dto.response;

import com.samteo.domain.myplanner.entity.PersonalPlanner;
import com.samteo.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AdminPlannerResponse {
    private String id;
    private String title;
    private String regionName;
    private String plannerType;
    private Long totalSalary;
    private Long disposableIncome;
    private OffsetDateTime createdAt;
    private Long userId;
    private String userName;
    private String userEmail;

    public static AdminPlannerResponse from(PersonalPlanner p, User user) {
        return AdminPlannerResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .regionName(p.getRegionName())
                .plannerType(p.getPlannerType())
                .totalSalary(p.getTotalSalary())
                .disposableIncome(p.getDisposableIncome())
                .createdAt(p.getCreatedAt())
                .userId(user != null ? user.getUserId() : p.getUserId())
                .userName(user != null ? user.getName() : "알 수 없음")
                .userEmail(user != null ? user.getEmail() : "-")
                .build();
    }
}
