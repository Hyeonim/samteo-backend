package com.samteo.domain.planner.repository;

import com.samteo.domain.planner.entity.Accommodation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, String> {

    List<Accommodation> findByRegionIdOrderByCommuteMinutesAsc(String regionId);

    List<Accommodation> findAllByOrderByCommuteMinutesAsc();

    @Query("SELECT a FROM Accommodation a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :kw, '%')) OR LOWER(a.district) LIKE LOWER(CONCAT('%', :kw, '%')) OR LOWER(a.address) LIKE LOWER(CONCAT('%', :kw, '%'))")
    Page<Accommodation> searchByKeyword(@Param("kw") String keyword, Pageable pageable);
}
