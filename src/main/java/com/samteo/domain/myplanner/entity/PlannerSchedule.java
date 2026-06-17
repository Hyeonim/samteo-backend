package com.samteo.domain.myplanner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

/**
 * 개인 플래너에 속하는 개별 스케줄 항목 엔티티이다.
 */
@Entity
@Table(name = "planner_schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlannerSchedule {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planner_id", nullable = false)
    private PersonalPlanner planner;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "day")
    private int day;

    @Column(name = "start_time", length = 5)
    private String startTime;

    @Column(name = "end_time", length = 5)
    private String endTime;

    @Column(name = "type_value", length = 100)
    private String typeValue;

    @Column(name = "type_label", length = 20)
    private String typeLabel;

    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;

    @Column(name = "date_key", length = 10)
    private String dateKey;

    @Column(name = "repeat_mode", length = 20)
    private String repeatMode;

    @Column(name = "locked")
    private boolean locked;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    /**
     * 스케줄 항목 인스턴스를 생성한다.
     *
     * @param planner    소속 플래너
     * @param id         클라이언트에서 생성한 UUID
     * @param title      스케줄 제목
     * @param day        요일 (0=월 ~ 6=일)
     * @param startTime  시작 시간 (HH:MM)
     * @param endTime    종료 시간 (HH:MM)
     * @param typeValue  이벤트 타입 값
     * @param typeLabel  이벤트 타입 레이블
     * @param color      표시 색상
     * @param memo       메모 (nullable)
     * @param dateKey    특정 날짜 키 (YYYY-MM-DD, nullable)
     * @param repeatMode 반복 모드 (기본값: weekly)
     * @param locked     잠금 여부
     * @return 생성된 스케줄 엔티티
     */
    public static PlannerSchedule of(
            PersonalPlanner planner,
            String id,
            String title,
            int day,
            String startTime,
            String endTime,
            String typeValue,
            String typeLabel,
            String color,
            String memo,
            String dateKey,
            String repeatMode,
            boolean locked
    ) {
        PlannerSchedule schedule = new PlannerSchedule();
        schedule.planner = planner;
        schedule.id = id;
        schedule.title = title;
        schedule.day = day;
        schedule.startTime = startTime;
        schedule.endTime = endTime;
        schedule.typeValue = typeValue;
        schedule.typeLabel = typeLabel;
        schedule.color = color;
        schedule.memo = memo;
        schedule.dateKey = dateKey;
        schedule.repeatMode = repeatMode != null ? repeatMode : "weekly";
        schedule.locked = locked;
        return schedule;
    }
}
