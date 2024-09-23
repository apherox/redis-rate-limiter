package com.ratelimiter.algorithms.login;

import com.ratelimiter.config.JedisConfig;
import redis.clients.jedis.exceptions.JedisException;

public class LoginRateLimiterFactory {

    private LoginRateLimiterFactory() {
    }

    public static LoginRateLimiter<String> createLoginRateLimiter() {
        try {
            return new RedisLoginRateLimiter(JedisConfig.getJedis());
        } catch (JedisException e) {
            // In case of Redis failure, fall back to a local rate limiter
            return new ConcurrentMapLoginRateLimiter();
        }
    }
}
