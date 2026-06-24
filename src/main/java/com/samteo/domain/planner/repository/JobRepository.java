package com.samteo.domain.planner.repository;

import com.samteo.domain.planner.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, String> {

    List<Job> findByRegionId(String regionId);

    List<Job> findByCityId(String cityId);

    Page<Job> findByCityId(String cityId, Pageable pageable);

    Page<Job> findByRegionId(String regionId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Job j SET j.bookmarkCount = j.bookmarkCount + 1 WHERE j.id = :id")
    void incrementBookmarkCount(@Param("id") String id);

    Page<Job> findAllByOrderByBookmarkCountDesc(Pageable pageable);
    Page<Job> findByCityIdOrderByBookmarkCountDesc(String cityId, Pageable pageable);
    Page<Job> findByRegionIdOrderByBookmarkCountDesc(String regionId, Pageable pageable);

    Page<Job> findAllByReviewCountGreaterThanOrderByReviewCountDesc(int reviewCount, Pageable pageable);
    Page<Job> findByCityIdAndReviewCountGreaterThanOrderByReviewCountDesc(String cityId, int reviewCount, Pageable pageable);
    Page<Job> findByRegionIdAndReviewCountGreaterThanOrderByReviewCountDesc(String regionId, int reviewCount, Pageable pageable);
}
