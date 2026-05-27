package com.samteo.domain.region.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
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
@Table(name = "regions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Region {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 500)
    private String summary;

    private int hotPlaceScore;
    private int housingCostScore;

    @Column(precision = 10, scale = 7)
    private BigDecimal centerLatitude;

    @Column(precision = 10, scale = 7)
    private BigDecimal centerLongitude;

    @ElementCollection
    @CollectionTable(name = "region_tags", joinColumns = @JoinColumn(name = "region_id"))
    @Column(name = "tag", nullable = false)
    private List<String> tags;

    @ElementCollection
    @CollectionTable(name = "region_recommendation_reasons", joinColumns = @JoinColumn(name = "region_id"))
    @Column(name = "reason", nullable = false, length = 500)
    private List<String> recommendationReasons;
}
