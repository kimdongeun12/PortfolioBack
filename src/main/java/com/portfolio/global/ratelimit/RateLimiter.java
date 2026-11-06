package com.portfolio.global.ratelimit;

import com.portfolio.global.exception.TooManyRequestsException;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter {

    private static final int CLEANUP_THRESHOLD = 10_000;

    private final Map<String, Window> windows = new ConcurrentHashMap<>();
    private final Clock clock;

    public RateLimiter() {
        this(Clock.systemUTC());
    }

    RateLimiter(Clock clock) {
        this.clock = clock;
    }

    public void check(String key, int limit, Duration window, String message) {
        if (!tryAcquire(key, limit, window)) {
            throw new TooManyRequestsException(message);
        }
    }

    public boolean tryAcquire(String key, int limit, Duration window) {
        Instant now = clock.instant();
        if (windows.size() > CLEANUP_THRESHOLD) {
            windows.entrySet().removeIf(entry -> !now.isBefore(entry.getValue().resetAt));
        }
        int[] count = new int[1];
        windows.compute(key, (k, current) -> {
            if (current == null || !now.isBefore(current.resetAt)) {
                current = new Window(now.plus(window));
            }
            current.count++;
            count[0] = current.count;
            return current;
        });
        return count[0] <= limit;
    }

    private static final class Window {
        private final Instant resetAt;
        private int count;

        private Window(Instant resetAt) {
            this.resetAt = resetAt;
        }
    }
}
