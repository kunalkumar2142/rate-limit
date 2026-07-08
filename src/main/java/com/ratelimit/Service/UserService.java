package com.ratelimit.Service;

import com.ratelimit.Model.User;
import com.ratelimit.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RedissonClient redissonClient;

    private static final String CACHE_NAME = "userList";

    public User getUser(String name) {
        RMapCache<String, User> cache = redissonClient.getMapCache(CACHE_NAME);
        User cachedUser = cache.get(name);
        if (cachedUser != null) {
            return cachedUser;
        }

        User user = userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User not found: " + name));

        cache.put(name, user, 60, TimeUnit.SECONDS);
        return user;
    }
}