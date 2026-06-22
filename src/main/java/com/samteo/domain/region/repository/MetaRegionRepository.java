package com.samteo.domain.region.repository;

import com.samteo.domain.region.entity.MetaRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetaRegionRepository extends JpaRepository<MetaRegion, Integer> {

    List<MetaRegion> findAllByOrderByIdAsc();
}
