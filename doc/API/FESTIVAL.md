# Festival API

Base path:

```text
/api
```

## Get Festivals

```http
GET /api/festivals?year=2026&month=6
```

Query parameters:

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| year | int | yes | Festival year |
| month | int | yes | Festival month |

Response shape:

```json
{
  "success": true,
  "data": []
}
```
