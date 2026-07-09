# API Rate Limiter

A Spring Boot service implementing per-user API rate limiting using the token bucket algorithm (Bucket4j), backed by Redis (Redisson) for distributed bucket state, with user-specific limits stored in MySQL.

## Tech Stack
- **Spring Boot 4.1** (Java 21)
- **MySQL** — stores per-user rate limit configuration
- **Redis + Redisson** — distributed cache and bucket state storage
- **Bucket4j** — token bucket rate limiting algorithm

## How It Works
## How It Works
1. Each user has a request limit defined in the `user_rate` table in MySQL.
2. User lookups are cached in Redis (via Redisson) with a TTL to avoid hitting the database on every request.
3. Requests to any `/api/v1/**` endpoint must include an `X-User-Id` header identifying the caller.
4. A servlet filter intercepts these requests and checks the caller's token bucket (stored in Redis via Bucket4j) before allowing the request through.
5. If the bucket has tokens available, the request proceeds and one token is consumed. If not, the request is rejected with `429 Too Many Requests`.
6. Each user's bucket refills its full configured limit at the start of every refill window (1 minute).

> Note: `/api/v2/**` endpoints are intentionally **not** rate-limited — useful for comparing protected vs unprotected behavior.


## Project Structure
```
com.ratelimit
├── Config        → Redis/Redisson and Bucket4j proxy manager configuration
├── Controller    → REST endpoints
├── Filter        → Rate limiting enforcement filter
├── Model         → JPA entities
├── Repository    → Spring Data JPA repositories
└── Service       → Business logic (user lookup, rate limiting)
```

## Setup

### 1. MySQL

Create the database:

```sql
CREATE DATABASE rate_limit_db;
```

Schema is auto-created by Hibernate (`ddl-auto=update`) on first run.

### 2. Redis
Run Redis locally via Docker:

```bash
docker run -d -p 6379:6379 redis
```

### 3. Configure `application.properties`

```properties
spring.application.name=rate-limit
server.port=9090

spring.datasource.url=jdbc:mysql://localhost:3306/rate_limit_db
spring.datasource.username={your_db_username}
spring.datasource.password={your_db_password}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.redis.host=localhost
spring.redis.port=6379
caching.spring.userListTTL=60000
```

### 4. Run the application

```bash
mvn spring-boot:run
```

### 5. Add test users

```sql
INSERT INTO user_rate (name, request_limit) VALUES
('user1', 5),
('user2', 10),
('premium1', 100);
```

## Usage

**Endpoint:**
```
GET :  /api/v1/user
Header:  X-User-Id : user1
```

**Behavior:**

| Scenario | Response |
|---|---|
| Valid header + tokens available | `200 OK` |
| Valid header + no tokens left | `429 Too Many Requests` |
| Missing header | `403 Forbidden` |

## Example

```bash
curl -H "X-User-Id: user1" http://localhost:9090/api/v1/user
```

Repeat this call more than 5 times within a minute (for `user1`, limit = 5) to trigger the rate limit and receive `429 Too Many Requests`.
