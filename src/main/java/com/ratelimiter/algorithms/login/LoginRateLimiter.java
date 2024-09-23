package com.ratelimiter.algorithms.login;

import com.ratelimiter.algorithms.RateLimiter;

public interface LoginRateLimiter<T> extends RateLimiter<T> {

    void incrementFailedAttempts(T param);

    void resetAttempts(T param);
}
