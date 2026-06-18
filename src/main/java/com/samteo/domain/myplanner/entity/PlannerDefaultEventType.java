package com.samteo.domain.myplanner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "planner_default_event_types")
@Getter
@NoArgsConstructor
public class PlannerDefaultEventType {

    @Id
    @Column(name = "value", length = 100)
    private String value;

    @Column(name = "label", nullable = false, length = 20)
    private String label;

    @Column(name = "color", nullable = false, length = 20)
    private String color;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
}
