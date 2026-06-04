# Auth API

Base path:

```text
/api/auth
```

## Register

```http
POST /api/auth/register
```

Request:

```json
{
  "email": "user@example.com",
  "password": "1234",
  "name": "User Name"
}
```

Response:

```json
{
  "token": "jwt-token",
  "userId": 1,
  "email": "user@example.com",
  "name": "User Name"
}
```

## Login

```http
POST /api/auth/login
```

Request:

```json
{
  "email": "planner@samteo.local",
  "password": "1234"
}
```

Response:

```json
{
  "token": "jwt-token",
  "userId": 1,
  "email": "planner@samteo.local",
  "name": "Planner Demo"
}
```

## Kakao OAuth Callback

```http
GET /login/oauth2/code/kakao?code={authorization_code}
```

The backend exchanges the Kakao authorization code, logs in or creates the user, then redirects to:

```text
{app.frontend-url}/oauth/callback?token={jwt-token}
```
