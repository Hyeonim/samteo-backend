package com.samteo.domain.user.repository;

import com.samteo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 공통 사용자 애그리게이트에 대한 영속성 접근을 제공한다.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 이메일이 정확히 일치하는 사용자를 조회한다.
     *
     * @param email 사용자 이메일
     * @return 조회된 사용자가 있으면 해당 사용자
     */
    Optional<User> findByEmail(String email);

    /**
     * 대소문자를 구분하지 않고 이메일로 사용자를 조회한다.
     *
     * @param email 사용자 이메일
     * @return 조회된 사용자가 있으면 해당 사용자
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * OAuth 제공자와 제공자별 식별자로 사용자를 조회한다.
     *
     * @param provider OAuth 제공자 이름
     * @param providerId 제공자가 발급한 사용자 고유 식별자
     * @return 조회된 사용자가 있으면 해당 사용자
     */
    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    @Query(value = """
            SELECT DATE(created_at), COUNT(*)
            FROM users
            WHERE created_at >= :from
            GROUP BY DATE(created_at)
            ORDER BY DATE(created_at)
            """, nativeQuery = true)
    List<Object[]> countDailyCreatedSince(@Param("from") LocalDateTime from);
}
