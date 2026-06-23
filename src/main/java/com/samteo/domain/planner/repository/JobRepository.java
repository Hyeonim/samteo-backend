package com.samteo.domain.planner.repository;

import com.samteo.domain.planner.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, String> {

    List<Job> findByRegionId(String regionId);

    List<Job> findByCityId(String cityId);

    Page<Job> findByCityId(String cityId, Pageable pageable);

    Page<Job> findByRegionId(String regionId, Pageable pageable);
}
