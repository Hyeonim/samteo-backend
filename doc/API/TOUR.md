# Tour API

## 서비스 콘텐츠

- `GET /api/festivals`: 축제 목록
- `GET /api/attractions`: 관광지 목록
- `GET /api/restaurants`: 음식점 목록

## TourAPI 중계

- `GET /api/tour/spots`
- `GET /api/tour/festivals`
- `GET /api/tour/detail/common?contentId={id}`
- `GET /api/tour/detail/intro?contentId={id}&contentTypeId={type}`

목록 API는 페이지, 지역 코드, 검색 조건을 쿼리 파라미터로 전달한다. 외부 API 키는
`TOUR_API_KEY` 환경변수에서 주입한다.
