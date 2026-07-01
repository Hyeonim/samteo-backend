# My Planner API

Base path: `/api/my-planner`

모든 API는 인증이 필요하며 사용자 소유권을 검사한다.

| Method | Path | 설명 |
| --- | --- | --- |
| GET | `/api/my-planner` | 내 플래너 전체 조회 |
| POST | `/api/my-planner` | 플래너 저장 |
| PUT | `/api/my-planner/{id}` | 플래너 수정 |
| DELETE | `/api/my-planner/{id}` | 플래너 삭제 |

저장 데이터에는 제목, 메모, 일정 유형, 월간 일정, 지역, 숙소비, 급여, 예상 잔액,
숙소, 일자리, 플래너 유형이 포함된다.
