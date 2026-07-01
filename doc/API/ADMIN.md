# Admin API

Base path: `/api/admin`

모든 API는 `ADMIN` 역할이 필요하다.

| Method | Path | 설명 |
| --- | --- | --- |
| GET | `/stats?days=7` | 기간별 대시보드 통계 |
| GET | `/users?page=0&size=20` | 회원 목록 |
| PUT | `/users/{id}/role` | 회원 역할 변경 |
| GET | `/jobs?page=0&size=20` | 일자리 목록 |
| DELETE | `/jobs/{id}` | 일자리 삭제 |
| GET | `/accommodations?page=0&size=20` | 숙소 목록 |
| DELETE | `/accommodations/{id}` | 숙소 삭제 |
| GET | `/planners?page=0&size=20` | 플래너 현황 |
| GET | `/community/posts?page=0&size=20&keyword=` | 게시글 관리 목록 |
| PUT | `/community/posts/{postId}` | 게시글 본문 수정 |
| DELETE | `/community/posts/{postId}` | 게시글 삭제 |
| GET | `/api-status` | 외부 API 연결 상태 |
