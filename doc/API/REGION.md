# Region API

Base path:

```text
/api/regions
```

## Get Regions

```http
GET /api/regions
```

Returns all supported regions.

## Recommend Regions

```http
POST /api/regions/recommendations
```

Request:

```json
{
  "option": "budget",
  "district": "Daegu",
  "needs": ["low-cost", "transit"]
}
```

The request body is optional. When omitted, the service returns default recommendations.
