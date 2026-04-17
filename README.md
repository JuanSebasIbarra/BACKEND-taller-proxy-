# AI Consumption Platform Backend

Backend built with Spring Boot 3 and Java 17.

## Features

- Proxy chain: `RateLimitGuard -> QuotaGuard -> MockTextGenerationService`
- Plan limits: `FREE`, `PRO`, `ENTERPRISE`
- In-memory concurrent usage tracking with `ConcurrentHashMap`
- Scheduled resets for minute and monthly counters
- REST endpoints for generate, quota status/history, and upgrade

## Run locally

```bash
./mvnw spring-boot:run
```

## Run tests

```bash
./mvnw test
```

## Main endpoints

- `POST /api/ai/generate`
- `GET /api/quota/status?userId={id}`
- `GET /api/quota/history?userId={id}`
- `POST /api/quota/upgrade`

## Railway env vars

- `PORT`
- `CORS_ALLOWED_ORIGINS`
- `UPGRADE_URL`
- `PLAN_FREE_RPM`
- `PLAN_FREE_TOKENS_MONTH`
- `PLAN_PRO_RPM`
- `PLAN_PRO_TOKENS_MONTH`

