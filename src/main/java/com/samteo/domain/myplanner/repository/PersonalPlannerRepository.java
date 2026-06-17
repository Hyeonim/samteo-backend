package com.samteo.domain.myplanner.repository;

import com.samteo.domain.myplanner.entity.PersonalPlanner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 개인 플래너 엔티티에 대한 JPA 저장소 인터페이스이다.
 */
public interface PersonalPlannerRepository extends JpaRepository<PersonalPlanner, String> {

    /**
     * 특정 사용자가 소유한 플래너 목록을 전체 조회한다.
     *
     * @param userId 조회할 사용자 ID
     * @return 해당 사용자의 플래너 목록
     */
    List<PersonalPlanner> findAllByUserId(Long userId);

    /**
     * 플래너 ID와 사용자 ID로 단일 플래너를 조회하여 소유권을 검증한다.
     *
     * @param id     플래너 UUID
     * @param userId 소유자 사용자 ID
     * @return 조건에 맞는 플래너 (Optional)
     */
    Optional<PersonalPlanner> findByIdAndUserId(String id, Long userId);
}
