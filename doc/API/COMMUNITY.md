# Community API

Base path: `/api/community/posts`

## 피드 조회

- `GET /api/community/posts?page=0&size=12`
- `GET /api/community/posts/{postId}`
- `GET /api/community/posts/me?page=0&size=12` (인증 필요)
- `GET /api/community/posts/users/{userId}?page=0&size=12`
- `GET /api/community/posts/tags/{tag}?page=0&size=9`

## 게시글 작성

```http
POST /api/community/posts
Content-Type: multipart/form-data
```

| Part | Type | 설명 |
| --- | --- | --- |
| `content` | string | 본문. `#태그` 포함 가능 |
| `images` | file[] | 복수 이미지 |

본문이나 이미지 중 하나는 반드시 있어야 한다.

## 게시글 관리

- `PUT /api/community/posts/{postId}`: 본인 게시글 본문 수정
- `DELETE /api/community/posts/{postId}`: 본인 게시글 소프트 삭제

## 좋아요

- `POST /api/community/posts/{postId}/likes`
- `DELETE /api/community/posts/{postId}/likes`

다른 사용자가 좋아요를 추가하면 게시글 작성자에게 알림을 생성한다.

## 댓글

- `GET /api/community/posts/{postId}/comments?page=0&size=20`
- `POST /api/community/posts/{postId}/comments`
- `DELETE /api/community/posts/{postId}/comments/{commentId}`

댓글 요청:

```json
{ "content": "좋은 정보 감사합니다." }
```
