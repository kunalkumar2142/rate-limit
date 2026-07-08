package com.ratelimit.Service;

import com.ratelimit.Model.User;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.distributed.BucketProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class RateLimiter {

    private final UserService userService;
    private final ProxyManager<String> proxyManager;

    public boolean tryConsume(String name) {
        BucketProxy bucket = proxyManager.builder()
                .build(name, getConfigSupplierForUser(name));
        return bucket.tryConsume(1);
    }

    private Supplier<BucketConfiguration> getConfigSupplierForUser(String name) {
        return () -> {
            User user = userService.getUser(name);
            Bandwidth limit = Bandwidth.classic(
                    user.getLimit(),
                    Refill.intervally(user.getLimit(), Duration.ofMinutes(1))
            );
            return BucketConfiguration.builder()
                    .addLimit(limit)
                    .build();
        };
    }
}