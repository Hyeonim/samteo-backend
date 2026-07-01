# 개발·배포 흐름서

최종 갱신일: 2026-07-01

## 로컬 개발

1. MariaDB를 `docker compose up -d`로 실행한다.
2. 백엔드를 dev 프로필로 실행한다.
3. 프론트엔드를 Vite 개발 서버로 실행한다.
4. 로컬 이미지 업로드는 Git 추적 경로가 아닌 임시 업로드 경로를 사용한다.

자세한 명령은 [`../RUN_GUIDE.md`](../RUN_GUIDE.md)를 참조한다.

## 기능 변경 체크리스트

1. 도메인 패키지의 기능명세를 갱신한다.
2. 컨트롤러 변경 시 `doc/API`를 갱신한다.
3. 엔티티·테이블 변경 시 SQL과 `doc/SCHEMA`를 함께 갱신한다.
4. 사용자 흐름이 바뀌면 `doc/MANUAL`을 갱신한다.
5. 백엔드 테스트와 프론트 빌드를 수행한다.

## 운영 배포

1. 민감 정보는 GitHub Secrets, 일반 설정은 Variables로 관리한다.
2. 운영 이미지는 GitHub Actions에서 빌드해 배포한다.
3. DB 변경은 `DDL_AUTO=update` 또는 검토된 SQL로 반영한다.
4. 배포 후 `/api/health`와 주요 인증 API를 확인한다.
5. CORS에는 프론트 도메인과 사용하는 HTTP 메서드가 모두 포함돼야 한다.

## 문서 보안

- 실제 비밀번호, OAuth Secret, JWT Secret, AWS Secret Key를 문서에 기록하지 않는다.
- 환경변수는 키 이름과 목적만 기록한다.
