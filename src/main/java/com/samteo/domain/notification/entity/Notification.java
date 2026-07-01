package com.samteo.domain.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "notifications",
        uniqueConstraints = @UniqueConstraint(name = "uk_notifications_dedupe_key", columnNames = "dedupe_key")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "actor_user_id")
    private Long actorUserId;

    @Column(name = "type", nullable = false, length = 40)
    private String type;

    @Column(name = "title", nullable = false, length = 120)
    private String title;

    @Column(name = "message", nullable = false, length = 500)
    private String message;

    @Column(name = "target_type", length = 40)
    private String targetType;

    @Column(name = "target_id", length = 100)
    private String targetId;

    @Column(name = "dedupe_key", length = 190)
    private String dedupeKey;

    @Column(name = "read_at")
    private OffsetDateTime readAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
        if (expiresAt == null) expiresAt = createdAt.plusDays(1);
    }

    public static Notification create(
            Long userId,
            Long actorUserId,
            String type,
            String title,
            String message,
            String targetType,
            String targetId,
            String dedupeKey,
            OffsetDateTime occurredAt
    ) {
        Notification notification = new Notification();
        notification.userId = userId;
        notification.actorUserId = actorUserId;
        notification.type = type;
        notification.title = title;
        notification.message = message;
        notification.targetType = targetType;
        notification.targetId = targetId;
        notification.dedupeKey = dedupeKey;
        notification.createdAt = occurredAt == null ? OffsetDateTime.now() : occurredAt;
        notification.expiresAt = notification.createdAt.plusDays(1);
        return notification;
    }

    public void markAsRead() {
        if (readAt == null) readAt = OffsetDateTime.now();
    }
}
