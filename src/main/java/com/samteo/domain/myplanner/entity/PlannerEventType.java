package com.samteo.domain.myplanner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 개인 플래너에 속하는 이벤트 타입 정의 엔티티이다.
 */
@Entity
@Table(name = "planner_event_types")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlannerEventType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planner_id", nullable = false)
    private PersonalPlanner planner;

    @Column(name = "value", length = 100)
    private String value;

    @Column(name = "label", length = 20)
    private String label;

    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "sort_order")
    private int sortOrder;

    /**
     * 이벤트 타입 인스턴스를 생성한다.
     *
     * @param planner   소속 플래너
     * @param value     이벤트 타입 값 (식별자)
     * @param label     표시 레이블
     * @param color     표시 색상
     * @param sortOrder 정렬 순서
     * @return 생성된 이벤트 타입 엔티티
     */
    public static PlannerEventType of(PersonalPlanner planner, String value, String label, String color, int sortOrder) {
        PlannerEventType eventType = new PlannerEventType();
        eventType.planner = planner;
        eventType.value = value;
        eventType.label = label;
        eventType.color = color;
        eventType.sortOrder = sortOrder;
        return eventType;
    }
}
