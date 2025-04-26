# PickSpot API

## Description
A simple API to find the best yard slot for a container based on scoring rules.

## How to Run
1. Install Java and Maven.
2. Clone the repository.
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## Endpoint
### POST `/api/pickSpot`

#### Request:
```json
{
  "container": {
    "id": "C1",
    "size": "small",
    "needsCold": false,
    "x": 1,
    "y": 1
  },
  "yardMap": [
    { "x": 1, "y": 2, "sizeCap": "small", "hasColdUnit": false, "occupied": false },
    { "x": 2, "y": 2, "sizeCap": "big", "hasColdUnit": true, "occupied": false }
  ]
}
```

#### Response:
```json
{
  "containerId": "C1",
  "targetX": 1,
  "targetY": 2
}
```

If no suitable slot is found:
```json
{
  "error": "no suitable slot"
}
```