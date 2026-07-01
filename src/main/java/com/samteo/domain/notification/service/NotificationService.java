package com.samteo.domain.notification.service;

import com.samteo.domain.myplanner.entity.PlannerSchedule;
import com.samteo.domain.myplanner.repository.PlannerScheduleRepository;
import com.samteo.domain.notification.dto.NotificationListResponse;
import com.samteo.domain.notification.dto.NotificationResponse;
import com.samteo.domain.notification.entity.Notification;
import com.samteo.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final ZoneId SERVICE_ZONE = ZoneId.of("Asia/Seoul");
    private static final String TARGET_COMMUNITY_POST = "COMMUNITY_POST";
    private static final String TARGET_PLANNER = "PLANNER";
    private static final String TARGET_USER_PROFILE = "USER_PROFILE";

    private final NotificationRepository notificationRepository;
    private final PlannerScheduleRepository plannerScheduleRepository;

    @Transactional
    public NotificationListResponse getNotifications(Long userId) {
        OffsetDateTime now = OffsetDateTime.now();
        notificationRepository.deleteByExpiresAtLessThanEqual(now);
        createDueScheduleNotifications(userId);

        return new NotificationListResponse(
                notificationRepository.findTop30ByUserIdAndExpiresAtAfterOrderByCreatedAtDesc(userId, now)
                        .stream()
                        .map(NotificationResponse::from)
                        .toList(),
                notificationRepository.countByUserIdAndReadAtIsNullAndExpiresAtAfter(userId, now)
        );
    }

    @Transactional
    public NotificationResponse markAsRead(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findByNotificationIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));
        notification.markAsRead();
        return NotificationResponse.from(notification);
    }

    @Transactional
    public int markAllAsRead(Long userId) {
        return notificationRepository.markAllAsRead(userId, OffsetDateTime.now());
    }

    @Transactional
    public void notifyPlannerCreated(Long userId, String plannerId, String plannerTitle) {
        create(
                userId,
                userId,
                "PLANNER_CREATED",
                "플래너가 생성됐어요",
                safeName(plannerTitle, "새 플래너") + " 플래너를 바로 확인해 보세요.",
                TARGET_PLANNER,
                plannerId,
                "planner-created:" + plannerId,
                null
        );
    }

    @Transactional
    public void notifyPostCreated(Long userId, Long postId) {
        create(
                userId,
                userId,
                "COMMUNITY_POST_CREATED",
                "게시글이 등록됐어요",
                "커뮤니티에 새로운 게시글이 공개됐습니다.",
                TARGET_COMMUNITY_POST,
                String.valueOf(postId),
                "post-created:" + postId,
                null
        );
    }

    @Transactional
    public void notifyPostLiked(Long recipientId, Long actorId, String actorName, Long postId) {
        if (recipientId.equals(actorId)) return;
        create(
                recipientId,
                actorId,
                "COMMUNITY_LIKE",
                "새로운 좋아요",
                safeName(actorName, "회원") + "님이 회원님의 게시글을 좋아합니다.",
                TARGET_COMMUNITY_POST,
                String.valueOf(postId),
                "post-like:" + postId + ":" + actorId,
                null
        );
    }

    @Transactional
    public void notifyPostCommented(
            Long recipientId,
            Long actorId,
            String actorName,
            Long postId,
            Long commentId
    ) {
        if (recipientId.equals(actorId)) return;
        create(
                recipientId,
                actorId,
                "COMMUNITY_COMMENT",
                "새로운 댓글",
                safeName(actorName, "회원") + "님이 회원님의 게시글에 댓글을 남겼습니다.",
                TARGET_COMMUNITY_POST,
                String.valueOf(postId),
                "post-comment:" + commentId,
                null
        );
    }

    @Transactional
    public void notifyFollowed(Long recipientId, Long actorId, String actorName) {
        if (recipientId.equals(actorId)) return;
        create(
                recipientId,
                actorId,
                "NEW_FOLLOWER",
                "새로운 팔로워",
                safeName(actorName, "회원") + "님이 회원님을 팔로우하기 시작했습니다.",
                TARGET_USER_PROFILE,
                String.valueOf(actorId),
                "follow:" + recipientId + ":" + actorId,
                null
        );
    }

    private void createDueScheduleNotifications(Long userId) {
        ZonedDateTime now = ZonedDateTime.now(SERVICE_ZONE);
        ZonedDateTime oldest = now.minusDays(1);

        for (PlannerSchedule schedule : plannerScheduleRepository.findAllByPlannerUserId(userId)) {
            ZonedDateTime scheduledAt = resolveScheduledAt(schedule, now.toLocalDate());
            if (scheduledAt == null || scheduledAt.isAfter(now) || !scheduledAt.isAfter(oldest)) continue;

            String occurrenceDate = scheduledAt.toLocalDate().toString();
            create(
                    userId,
                    null,
                    "PLANNER_SCHEDULE",
                    "플래너 일정이 시작됐어요",
                    safeName(schedule.getTitle(), "등록한 일정") + " 일정을 확인해 보세요.",
                    TARGET_PLANNER,
                    schedule.getPlanner().getId(),
                    "schedule:" + schedule.getId() + ":" + occurrenceDate,
                    scheduledAt.toOffsetDateTime()
            );
        }
    }

    private ZonedDateTime resolveScheduledAt(PlannerSchedule schedule, LocalDate today) {
        LocalTime startTime;
        try {
            startTime = LocalTime.parse(schedule.getStartTime());
        } catch (DateTimeParseException | NullPointerException exception) {
            return null;
        }

        LocalDate targetDate;
        if (schedule.getDateKey() != null && !schedule.getDateKey().isBlank()) {
            try {
                targetDate = LocalDate.parse(schedule.getDateKey());
            } catch (DateTimeParseException exception) {
                return null;
            }
        } else {
            int dayOffset = Math.max(0, Math.min(schedule.getDay(), 6));
            LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            targetDate = monday.plusDays(dayOffset);
        }
        return ZonedDateTime.of(targetDate, startTime, SERVICE_ZONE);
    }

    private void create(
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
        if (dedupeKey != null && notificationRepository.existsByDedupeKey(dedupeKey)) return;
        notificationRepository.save(Notification.create(
                userId,
                actorUserId,
                type,
                title,
                message,
                targetType,
                targetId,
                dedupeKey,
                occurredAt
        ));
    }

    private String safeName(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }
}
