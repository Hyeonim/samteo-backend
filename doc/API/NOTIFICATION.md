# Notification API

Base path: `/api/notifications`

모든 API는 인증이 필요하다. 알림은 생성 후 24시간 동안만 제공된다.

## 알림 목록

```http
GET /api/notifications
```

응답 데이터:

```json
{
  "notifications": [
    {
      "notificationId": 1,
      "type": "COMMUNITY_COMMENT",
      "title": "새로운 댓글",
      "message": "테스트님이 회원님의 게시글에 댓글을 남겼습니다.",
      "targetType": "COMMUNITY_POST",
      "targetId": "12",
      "read": false,
      "createdAt": "2026-07-01T14:30:00+09:00",
      "expiresAt": "2026-07-02T14:30:00+09:00"
    }
  ],
  "unreadCount": 1
}
```

## 개별 읽음

```http
PATCH /api/notifications/{notificationId}/read
```

## 모두 읽음

```http
PATCH /api/notifications/read-all
```

응답 데이터는 읽음 처리된 행 수이다.
