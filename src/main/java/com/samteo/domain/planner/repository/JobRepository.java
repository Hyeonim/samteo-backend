package com.samteo.domain.planner.repository;

import com.samteo.domain.planner.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, String> {

    List<Job> findByRegionId(String regionId);
}
