# Auth API

최종 갱신일: 2026-07-01

## 로컬 회원가입

```http
POST /api/auth/register
```

```json
{
  "email": "user@example.com",
  "password": "password",
  "name": "회원명"
}
```

## 로컬 로그인

```http
POST /api/auth/login
```

요청은 이메일과 비밀번호를 사용하며, 성공 시 JWT와 사용자 정보를 반환한다.

## OAuth 로그인

| 공급자 | Callback |
| --- | --- |
| Kakao | `GET /login/oauth2/code/kakao?code={code}` |
| Google | `GET /login/oauth2/code/google?code={code}` |
| Naver | `GET /login/oauth2/code/naver?code={code}&state={state}` |

처리 흐름:

1. 인가 코드로 공급자 access token을 교환한다.
2. 공급자 사용자 ID로 `user_auth_identities`를 조회한다.
3. 연결된 사용자가 있으면 JWT를 발급한다.
4. 동일 이메일 계정 연결 확인이 필요하면 pending token을 발급한다.
5. 성공 후 `{app.frontend-url}/oauth/callback`으로 이동한다.

## OAuth 계정 연결

```http
POST /api/users/me/auth-identities/link
Authorization: Bearer {jwt}
```

```json
{
  "pendingToken": "oauth-link-pending-token"
}
```

같은 사용자는 이메일 문자열만으로 자동 병합하지 않고, 로그인 공급자 ID와 명시적 연결 절차로 통합한다.

## JWT 만료

- 기본 만료시간은 `jwt.expiration` 설정을 따른다.
- 만료 토큰 요청은 401과 `로그인이 만료되었거나 필요합니다.` 메시지를 반환한다.
- 프론트는 저장된 인증 정보를 제거하고 재로그인 안내를 표시한다.
