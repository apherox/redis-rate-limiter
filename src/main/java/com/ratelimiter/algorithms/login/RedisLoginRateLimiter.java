package com.ratelimiter.algorithms.login;

import lombok.RequiredArgsConstructor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * This class throttles the requests which go to the /login page
 * Maximum of {@link RedisLoginRateLimiter#MAX_ATTEMPTS} are allowed
 * Time window for blocking all requests after all {@link RedisLoginRateLimiter#MAX_ATTEMPTS} are exhausted is
 * defined to be {@link RedisLoginRateLimiter#TIME_WINDOW} seconds
 */
@RequiredArgsConstructor
final class RedisLoginRateLimiter implements LoginRateLimiter<String> {

    // Configurable limit and time window (in seconds)
    private static final int MAX_ATTEMPTS = 5;
    private static final int TIME_WINDOW = 15 * 60; // 15 minutes (in seconds)

    private final Jedis jedis;

    @Override
    public boolean isRateLimited(String username) {
        String key = "login_attempts:" + username;
        String attempts = jedis.get(key);

        if (attempts == null) {
            return false; // No attempts recorded
        }

        int attemptCount = Integer.parseInt(attempts);
        return attemptCount >= MAX_ATTEMPTS;
    }

    @Override
    public void incrementFailedAttempts(String username) {
        String key = "login_attempts:" + username;

        // Use Redis transactions to ensure atomic operations
        jedis.watch(key);
        String attempts = jedis.get(key);

        if (attempts == null) {
            Transaction t = jedis.multi();
            t.set(key, "1");
            t.expire(key, TIME_WINDOW); // Set expiration time on first attempt
            t.exec();
        } else {
            int attemptCount = Integer.parseInt(attempts);
            Transaction t = jedis.multi();
            t.set(key, String.valueOf(attemptCount + 1));
            t.exec();
        }
    }

    /**
     * Clears the failed attempts after successful login
     */
    @Override
    public void resetAttempts(String username) {
        String key = "login_attempts:" + username;
        jedis.del(key);
    }
}