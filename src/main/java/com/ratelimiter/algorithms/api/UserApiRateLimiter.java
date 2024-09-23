package com.ratelimiter.algorithms.api;

import com.ratelimiter.algorithms.RateLimiter;

/**
 * Implements user based throttling on API usage (accessing /api endpoint)
 */
public class UserApiRateLimiter implements RateLimiter<String> {

    @Override
    public boolean isRateLimited(String param) {
        // TODO implement user based rate limiting on API usage
        return false;
    }

}
