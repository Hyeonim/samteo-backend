package com.samteo.domain.planner.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@Entity
@Table(name = "accommodations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Accommodation {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 50)
    private String regionId;

    @Column(nullable = false, length = 100)
    private String district;

    @Column(nullable = false, length = 255)
    private String address;

    private int monthlyPrice;
    private int deposit;

    @Column(precision = 6, scale = 2)
    private BigDecimal distanceKm;

    private int commuteMinutes;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "accommodation_tags", joinColumns = @JoinColumn(name = "accommodation_id"))
    @Column(name = "tag", nullable = false)
    private List<String> tags;

    @Column(length = 30)
    private String color;

    private Integer rank;
}
