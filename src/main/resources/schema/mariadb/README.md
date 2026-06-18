# MariaDB 시드 구성

로컬 Docker MariaDB는 아래 SQL 그룹을 순서대로 실행해서 초기화한다.

1. `prod/`
   - 운영 DB dump를 기반으로 만든 기준 데이터이다.
   - 현재 로컬 Docker는 `prod/00_prod_baseline_dump.sql`을 가장 먼저 실행한다.
   - 직접 작성한 로컬 테스트 데이터가 아니라, 운영 DB 스냅샷으로 취급한다.

2. `local-dev/`
   - 아직 운영 DB에 반영되지 않았거나, 로컬 개발과 테스트에 필요한 추가 SQL이다.
   - 운영 dump 적용 후 아래 순서로 실행된다.
     - `10_local_dev_schema_delta.sql`
     - `20_local_dev_seed_delta.sql`
     - `30_local_dev_tour_api_compat_views.sql`
   - 운영 DB에 정식 테이블이 추가되면, 같은 이름의 호환 view는 만들지 않는다.

3. `legacy-test/`
   - 이전에 로컬 테스트용으로 직접 작성했던 schema/seed SQL이다.
   - 참고용으로만 보관한다.
   - 현재 `docker-compose.yml`에는 마운트하지 않는다.

Docker는 `/docker-entrypoint-initdb.d`에 마운트된 SQL 파일을 파일명 순서대로 실행한다. 단, MariaDB 볼륨이 처음 생성될 때만 실행된다.

이 SQL 구성으로 로컬 DB를 다시 만들려면 기존 볼륨을 삭제한 뒤 다시 올린다.

```bash
docker compose down -v
docker compose up -d
```
