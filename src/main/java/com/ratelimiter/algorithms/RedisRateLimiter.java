package com.ratelimiter.algorithms;

import lombok.RequiredArgsConstructor;
import redis.clients.jedis.Jedis;

@RequiredArgsConstructor
public class RedisRateLimiter implements RateLimiter<Void> {

    private static final String RATE_LIMIT_KEY = "rate_limit";
    private static final long LIMIT_INTERVAL_MS = 2000; // 2 seconds

    private final Jedis jedis;

    @Override
    public boolean isRateLimited(Void param) {
        long currentTimeMillis = System.currentTimeMillis();
        String lastRequestTime = jedis.get(RATE_LIMIT_KEY);

        if (lastRequestTime == null) {
            jedis.set(RATE_LIMIT_KEY, String.valueOf(currentTimeMillis));
            return false;
        }

        long lastRequestTimeMillis = Long.parseLong(lastRequestTime);
        if (currentTimeMillis - lastRequestTimeMillis >= LIMIT_INTERVAL_MS) {
            jedis.set(RATE_LIMIT_KEY, String.valueOf(currentTimeMillis));
            return false;
        }

        return true;
    }

}
