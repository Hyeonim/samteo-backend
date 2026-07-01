package com.samteo.domain.myplanner.repository;

import com.samteo.domain.myplanner.entity.PlannerSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerScheduleRepository extends JpaRepository<PlannerSchedule, String> {

    List<PlannerSchedule> findAllByPlannerUserId(Long userId);
}
