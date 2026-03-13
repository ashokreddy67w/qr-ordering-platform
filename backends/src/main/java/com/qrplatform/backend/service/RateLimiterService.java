package com.qrplatform.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${rate-limiter.enabled:true}")
    private boolean enabled;

    public RateLimiterService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String key, int maxAttempts, long windowSeconds) {
        if (!enabled) {
            return true; // Rate limiting disabled
        }

        Long attempts = redisTemplate.opsForValue().increment(key);
        if (attempts == 1) {
            redisTemplate.expire(key, windowSeconds, TimeUnit.SECONDS);
        }
        return attempts <= maxAttempts;
    }

    public boolean isAllowed(String key, int maxAttempts, Duration window) {
        return isAllowed(key, maxAttempts, window.getSeconds());
    }
}