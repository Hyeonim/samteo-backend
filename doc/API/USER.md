# User API

## 내 정보

- `GET /api/users/me`: 회원명, 이메일, 역할, 연결된 로그인 공급자 조회
- `PUT /api/users/me`: 회원명과 이메일 수정

## 공개 프로필

```http
GET /api/users/{profileUserId}/profile
```

게시글 수, 팔로워 수, 팔로잉 수, 본인 여부, 현재 사용자의 팔로우 여부를 반환한다.

## 팔로우

- `POST /api/users/{profileUserId}/follow`
- `DELETE /api/users/{profileUserId}/follow`

본인은 팔로우할 수 없다. 신규 팔로우가 생성되면 대상 사용자에게 24시간 알림을 생성한다.
