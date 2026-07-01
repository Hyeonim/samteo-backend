package com.samteo.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.samteo.domain.notification.entity.Notification;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponse {

    private Long notificationId;
    private String type;
    private String title;
    private String message;
    private String targetType;
    private String targetId;
    @JsonProperty("read")
    private boolean read;
    private String createdAt;
    private String expiresAt;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .targetType(notification.getTargetType())
                .targetId(notification.getTargetId())
                .read(notification.getReadAt() != null)
                .createdAt(notification.getCreatedAt().toString())
                .expiresAt(notification.getExpiresAt().toString())
                .build();
    }
}
