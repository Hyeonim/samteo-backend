package com.samteo.domain.region.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "meta_region")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MetaRegion {

    @Id
    @Column(name = "region_id")
    private Integer id;

    @Column(name = "region_nm")
    private String name;
}
