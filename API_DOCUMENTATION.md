# Trade API Documentation

## Overview
This Spring Boot MCP server application provides a REST endpoint for retrieving trade data by date.

## Endpoints

### Get Trades by Date

**Endpoint**: `GET /api/v1/trades`

**Parameters**:
- `date` (required): Date in yyyy-MM-dd format (e.g., 2024-01-15)

**Response**: JSON object containing trade data

### Health Check

**Endpoint**: `GET /api/v1/trades/health`

**Response**: Health status of the Trade API service

## Sample Request & Response

### Successful Request

**Request**:
```bash
curl -X GET "http://localhost:8080/api/v1/trades?date=2024-01-15" \
  -H "Accept: application/json"
```

**Response** (HTTP 200):
```json
{
  "tradeDate": "2024-01-15",
  "totalTrades": 3,
  "trades": [
    {
      "tradeId": "TRD-20240115-0001",
      "symbol": "AAPL",
      "quantity": 100,
      "price": 175.50,
      "executionTime": "2024-01-15T09:30:00"
    },
    {
      "tradeId": "TRD-20240115-0002",
      "symbol": "GOOGL",
      "quantity": 50,
      "price": 131.20,
      "executionTime": "2024-01-15T10:15:00"
    },
    {
      "tradeId": "TRD-20240115-0003",
      "symbol": "MSFT",
      "quantity": 200,
      "price": 378.85,
      "executionTime": "2024-01-15T14:45:00"
    }
  ]
}
```

### Error Responses

#### Missing Date Parameter

**Request**:
```bash
curl -X GET "http://localhost:8080/api/v1/trades"
```

**Response** (HTTP 400):
```json
{
  "error": "Missing required parameter",
  "message": "Required request parameter 'date' for method parameter type LocalDate is not present",
  "parameter": "date"
}
```

#### Invalid Date Format

**Request**:
```bash
curl -X GET "http://localhost:8080/api/v1/trades?date=invalid-date"
```

**Response** (HTTP 400):
```json
{
  "error": "Invalid date format",
  "message": "Date must be in format yyyy-MM-dd (e.g., 2024-01-15)",
  "provided": "invalid-date"
}
```

#### Future Date (Invalid Trading Date)

**Request**:
```bash
curl -X GET "http://localhost:8080/api/v1/trades?date=2025-12-31"
```

**Response** (HTTP 400):
```json
{
  "error": "Invalid trading date",
  "message": "Date cannot be in the future or null",
  "date": "2025-12-31"
}
```

### Health Check

**Request**:
```bash
curl -X GET "http://localhost:8080/api/v1/trades/health"
```

**Response** (HTTP 200):
```json
{
  "status": "UP",
  "service": "Trade API",
  "timestamp": "2025-09-11T22:43:57.539601440"
}
```

## Trade Data Structure

Each trade object contains:
- `tradeId`: Unique identifier for the trade (format: TRD-YYYYMMDD-XXXX)
- `symbol`: Stock symbol (e.g., AAPL, GOOGL, MSFT)
- `quantity`: Number of shares traded (positive integer)
- `price`: Price per share (decimal with 2 decimal places)
- `executionTime`: Date and time when trade was executed (ISO format)

## Mock Data
The API returns mock trade data that simulates realistic trading scenarios:
- Weekdays typically have more trades than weekends
- Trades are generated during market hours (9:30 AM - 4:00 PM EST)
- Popular stock symbols with realistic price variations
- Random quantities and price fluctuations

## Running the Application

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

3. The application will start on port 8080

4. Test the API:
   ```bash
   curl "http://localhost:8080/api/v1/trades?date=2024-01-15"
   ```