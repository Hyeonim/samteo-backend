# User API

Base path:

```text
/api
```

## Get Me

```http
GET /api/users/me
```

Authorization:

```http
Authorization: Bearer {jwt-token}
```

Response shape:

```json
{
  "success": true,
  "data": {
    "id": 1,
    "email": "planner@samteo.local",
    "name": "Planner Demo"
  }
}
```
