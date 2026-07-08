# API Rate Limiter

A Spring Boot service implementing per-user API rate limiting using the token bucket algorithm (Bucket4j), backed by Redis (Redisson) for distributed bucket state, with user-specific limits stored in MySQL.

## Tech Stack
- **Spring Boot 4.1** (Java 21)
- **MySQL** — stores per-user rate limit configuration
- **Redis + Redisson** — distributed cache and bucket state storage
- **Bucket4j** — token bucket rate limiting algorithm

## How It Works
1. Each user has a request limit defined in the `user_rate` table in MySQL.
2. User lookups are cached in Redis (via Redisson) to avoid hitting the database on every request.
3. Requests to any `/v1/**` endpoint must include an `X-User-Id` header identifying the caller.
4. A servlet filter intercepts these requests and checks the caller's token bucket (stored in Redis via Bucket4j) before allowing the request through.
5. If the bucket has tokens available, the request proceeds and a token is consumed. If not, the request is rejected with `429 Too Many Requests`.
6. Tokens refill automatically over time based on each user's configured limit and refill window.

## Project Structure