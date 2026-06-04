# Job API

Base path:

```text
/api
```

## Get External Jobs

```http
GET /api/jobs?size=6
```

Query parameters:

| Name | Type | Required | Default | Description |
| --- | --- | --- | --- | --- |
| size | int | no | 6 | Number of job results |

Response shape:

```json
{
  "success": true,
  "data": []
}
```

This API uses the health domain job service and external job API configuration.
