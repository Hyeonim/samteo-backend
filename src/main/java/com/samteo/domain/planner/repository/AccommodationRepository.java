package com.samteo.domain.planner.repository;

import com.samteo.domain.planner.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, String> {

    List<Accommodation> findByRegionIdOrderByCommuteMinutesAsc(String regionId);

    List<Accommodation> findAllByOrderByCommuteMinutesAsc();
}
