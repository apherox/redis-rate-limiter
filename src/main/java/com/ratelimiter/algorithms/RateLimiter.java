package com.ratelimiter.algorithms;

@FunctionalInterface
public interface RateLimiter<T> {

    boolean isRateLimited(T param);
}
