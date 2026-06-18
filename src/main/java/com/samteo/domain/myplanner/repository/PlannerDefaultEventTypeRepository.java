package com.samteo.domain.myplanner.repository;

import com.samteo.domain.myplanner.entity.PlannerDefaultEventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerDefaultEventTypeRepository extends JpaRepository<PlannerDefaultEventType, String> {

    List<PlannerDefaultEventType> findAllByOrderBySortOrderAsc();
}
