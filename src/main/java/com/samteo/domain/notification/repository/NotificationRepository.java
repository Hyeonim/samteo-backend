package com.samteo.domain.notification.repository;

import com.samteo.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findTop30ByUserIdAndExpiresAtAfterOrderByCreatedAtDesc(
            Long userId,
            OffsetDateTime now
    );

    long countByUserIdAndReadAtIsNullAndExpiresAtAfter(Long userId, OffsetDateTime now);

    Optional<Notification> findByNotificationIdAndUserId(Long notificationId, Long userId);

    boolean existsByDedupeKey(String dedupeKey);

    long deleteByExpiresAtLessThanEqual(OffsetDateTime now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE Notification n
            SET n.readAt = :readAt
            WHERE n.userId = :userId
              AND n.readAt IS NULL
              AND n.expiresAt > :readAt
            """)
    int markAllAsRead(@Param("userId") Long userId, @Param("readAt") OffsetDateTime readAt);
}
