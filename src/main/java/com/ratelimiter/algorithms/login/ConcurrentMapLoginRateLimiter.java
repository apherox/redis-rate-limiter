package com.ratelimiter.algorithms.login;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Fallback rate limiter when redis database is not available
 */
final class ConcurrentMapLoginRateLimiter implements LoginRateLimiter<String> {

    private final Map<String, List<Long>> loginAttempts = new ConcurrentHashMap<>();

    // Configurable limit and time window (in milliseconds)
    private static final int MAX_ATTEMPTS = 5;
    private static final long TIME_WINDOW = 15 * 60 * 1000L; // 15 minutes

    @Override
    public boolean isRateLimited(String username) {
        List<Long> attempts = loginAttempts.getOrDefault(username, new ArrayList<>());
        long now = System.currentTimeMillis();

        attempts.removeIf(timestamp -> now - timestamp > TIME_WINDOW);

        loginAttempts.put(username, attempts);

        return attempts.size() >= MAX_ATTEMPTS;
    }

    @Override
    public void incrementFailedAttempts(String username) {
        List<Long> attempts = loginAttempts.getOrDefault(username, new ArrayList<>());
        attempts.add(System.currentTimeMillis());
        loginAttempts.put(username, attempts);
    }

    @Override
    public void resetAttempts(String username) {
        loginAttempts.remove(username); // Clear the login attempts for the user
    }
}
