# API 인덱스

최종 갱신일: 2026-07-01

## 공통

- 기본 응답: `{ "success": true, "data": ..., "message": null }`
- 인증: `Authorization: Bearer {jwt}`
- 공개 조회를 제외한 API는 JWT가 필요하다.
- 페이지 번호는 0부터 시작한다.

## 엔드포인트 목록

| 도메인 | Method | Path | 설명 |
| --- | --- | --- | --- |
| 인증 | POST | `/api/auth/register` | 로컬 회원가입 |
| 인증 | POST | `/api/auth/login` | 로컬 로그인 |
| 인증 | GET | `/login/oauth2/code/{kakao|google|naver}` | OAuth 콜백 |
| 인증 | POST | `/api/users/me/auth-identities/link` | OAuth 계정 연결 |
| 사용자 | GET | `/api/users/me` | 내 정보 |
| 사용자 | PUT | `/api/users/me` | 내 정보 수정 |
| 사용자 | GET | `/api/users/{id}/profile` | 공개 프로필 |
| 사용자 | POST | `/api/users/{id}/follow` | 팔로우 |
| 사용자 | DELETE | `/api/users/{id}/follow` | 언팔로우 |
| 지역 | GET | `/api/regions` | 지역 목록 |
| 지역 | POST | `/api/regions/recommendations` | 지역 추천 |
| 플래너 | GET | `/api/planner/bootstrap` | 생성 초기 데이터 |
| 플래너 | GET | `/api/planner/jobs` | 일자리 목록 |
| 플래너 | GET | `/api/planner/jobs/page` | 일자리 페이지 |
| 플래너 | GET | `/api/planner/accommodations` | 숙소 목록 |
| 플래너 | GET | `/api/planner/map-provider` | 지도 제공자 |
| 플래너 | POST | `/api/planner/budget` | 예산 계산 |
| 플래너 | POST | `/api/planner/transit-routes` | 대중교통 경로 |
| 플래너 | GET | `/api/planner/load-lane` | ODsay 노선 정보 |
| 플래너 | POST | `/api/planner/driving-route` | 운전 경로 |
| 플래너 | POST | `/api/planner/commute-route` | 통근 경로 |
| 플래너 | POST | `/api/planner` | 최종 플래너 생성 |
| 내 플래너 | GET | `/api/my-planner` | 내 플래너 목록 |
| 내 플래너 | POST | `/api/my-planner` | 개인 플래너 저장 |
| 내 플래너 | PUT | `/api/my-planner/{id}` | 개인 플래너 수정 |
| 내 플래너 | DELETE | `/api/my-planner/{id}` | 개인 플래너 삭제 |
| 커뮤니티 | GET | `/api/community/posts` | 피드 목록 |
| 커뮤니티 | POST | `/api/community/posts` | 게시글 작성 |
| 커뮤니티 | GET | `/api/community/posts/{id}` | 게시글 상세 |
| 커뮤니티 | PUT | `/api/community/posts/{id}` | 게시글 수정 |
| 커뮤니티 | DELETE | `/api/community/posts/{id}` | 게시글 삭제 |
| 커뮤니티 | GET | `/api/community/posts/me` | 내 게시글 |
| 커뮤니티 | GET | `/api/community/posts/users/{userId}` | 사용자별 피드 |
| 커뮤니티 | GET | `/api/community/posts/tags/{tag}` | 태그별 피드 |
| 커뮤니티 | POST/DELETE | `/api/community/posts/{id}/likes` | 좋아요/취소 |
| 커뮤니티 | GET/POST | `/api/community/posts/{id}/comments` | 댓글 조회/작성 |
| 커뮤니티 | DELETE | `/api/community/posts/{id}/comments/{commentId}` | 댓글 삭제 |
| 알림 | GET | `/api/notifications` | 최근 24시간 알림 |
| 알림 | PATCH | `/api/notifications/{id}/read` | 개별 읽음 |
| 알림 | PATCH | `/api/notifications/read-all` | 모두 읽음 |
| 관광 | GET | `/api/festivals` | 축제 목록 |
| 관광 | GET | `/api/attractions` | 관광지 목록 |
| 관광 | GET | `/api/restaurants` | 음식점 목록 |
| 관광 | GET | `/api/tour/spots` | TourAPI 관광지 |
| 관광 | GET | `/api/tour/festivals` | TourAPI 축제 |
| 관광 | GET | `/api/tour/detail/common` | 공통 상세 |
| 관광 | GET | `/api/tour/detail/intro` | 소개 상세 |
| 일자리 | GET | `/api/jobs` | 일자리 목록 |
| 채팅 | POST | `/api/chat` | 삼터 도우미 질문 |
| 상태 | GET | `/api/health` | 서버/DB 상태 |
| 관리자 | GET | `/api/admin/stats` | 대시보드 통계 |
| 관리자 | GET | `/api/admin/users` | 회원 목록 |
| 관리자 | PUT | `/api/admin/users/{id}/role` | 회원 역할 변경 |
| 관리자 | GET/DELETE | `/api/admin/jobs[/{id}]` | 일자리 관리 |
| 관리자 | GET/DELETE | `/api/admin/accommodations[/{id}]` | 숙소 관리 |
| 관리자 | GET | `/api/admin/planners` | 플래너 현황 |
| 관리자 | GET/PUT/DELETE | `/api/admin/community/posts[/{id}]` | 커뮤니티 관리 |
| 관리자 | GET | `/api/admin/api-status` | 외부 API 상태 |

세부 요청/응답은 같은 디렉터리의 도메인 문서를 참조한다.
