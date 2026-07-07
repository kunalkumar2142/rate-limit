# API Rate Limiting With Bucket4J and Redis

- I just implement api rate limiting in a scaled service. 
- here i am using [Bucket4J](https://github.com/bucket4j/bucket4j) library to implement it and [Redis](https://redis.io/) as a distributed cache
- Here we can also set diffrent Rate Limit by Users

### Problem with Unlimited Rates

If a public API is allowed its users to make an unlimited number of requests per hour, it could lead to:
- resource exhaustion
- decreasing quality of the service
- denial of service attacks
- This might result in a situation where the service is unavailable or slow. It could also lead to more unexpected costs being incurred by the service.

### Stay tuned is under process.....
