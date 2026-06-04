# Samteo Backend Documents

이 디렉터리는 Samteo 백엔드 문서 루트입니다.

기술, 기능, API, DB 스키마가 변경될 때는 코드 변경과 함께 이 디렉터리의 문서를 먼저 확인하고 갱신합니다.

## Structure

```text
doc/
  TECH_SPEC.md
  RUN_GUIDE.md
  API/
    AUTH.md
    HEALTH.md
    JOB.md
    PLANNER.md
    REGION.md
    USER.md
  SCHEMA/
    ERD.md
```

## Update Rule

- API가 변경되면 `doc/API` 하위 도메인 문서를 갱신합니다.
- DB 테이블, 컬럼, 관계, seed data가 변경되면 `doc/SCHEMA/ERD.md`와 `src/main/resources/schema/mariadb` SQL을 함께 갱신합니다.
- 서버 스펙, 배포 방식, 런타임, 외부 연동 정보가 변경되면 `doc/TECH_SPEC.md`를 갱신합니다.
- 로컬 실행 방법, 환경변수, Docker Compose 사용법이 변경되면 `doc/RUN_GUIDE.md`를 갱신합니다.
