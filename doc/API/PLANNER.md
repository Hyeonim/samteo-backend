# Planner API

Base path:

```text
/api/planner
```

## Bootstrap

```http
GET /api/planner/bootstrap
```

Returns planner bootstrap data such as regions, jobs, accommodations, and provider metadata.

## Jobs

```http
GET /api/planner/jobs?regionId=junggu
```

Query parameters:

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| regionId | string | no | Filter jobs by region |

## Accommodations

```http
GET /api/planner/accommodations?regionId=junggu
```

Query parameters:

| Name | Type | Required | Description |
| --- | --- | --- | --- |
| regionId | string | no | Filter accommodations by region |

## Map Provider

```http
GET /api/planner/map-provider
```

Returns the map provider metadata used by the frontend.

## Budget Simulation

```http
POST /api/planner/budget
```

Request:

```json
{
  "jobId": "job-001",
  "accommodationId": "acc-001",
  "transportCost": 80000,
  "foodCost": 300000,
  "extraCost": 0,
  "useRecommendedRoute": true
}
```

## Transit Routes

```http
POST /api/planner/transit-routes
```

Request:

```json
{
  "startAnchorType": "accommodation",
  "startAnchorId": "acc-001",
  "endAnchorType": "job",
  "endAnchorId": "job-001",
  "startName": "Daegu Downtown Hostel",
  "endName": "Dongseong-ro Bakery Cafe Staff",
  "startLatitude": 35.8714,
  "startLongitude": 128.6014,
  "endLatitude": 35.8698,
  "endLongitude": 128.594,
  "sort": 0,
  "searchType": 0,
  "pathType": 0,
  "language": 0
}
```

## Commute Route

```http
POST /api/planner/commute-route
```

Request:

```json
{
  "accommodationId": "acc-001",
  "jobId": "job-001",
  "sort": 0,
  "searchType": 0,
  "pathType": 0,
  "language": 0
}
```

## Create Planner

```http
POST /api/planner
```

Request:

```json
{
  "regionId": "junggu",
  "jobIds": ["job-001"],
  "accommodationId": "acc-001",
  "startDate": "2026-06-01",
  "months": 1,
  "transportCost": 80000,
  "foodCost": 300000,
  "extraCost": 0
}
```
