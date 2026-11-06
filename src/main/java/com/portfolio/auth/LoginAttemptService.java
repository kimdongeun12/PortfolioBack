package com.portfolio.auth;

import com.portfolio.global.exception.TooManyRequestsException;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginAttemptService {

    static final int MAX_FAILURES_PER_IP = 5;
    static final Duration WINDOW = Duration.ofMinutes(15);
    private static final int CLEANUP_THRESHOLD = 10_000;

    private final Map<String, Attempt> attempts = new ConcurrentHashMap<>();
    private final Clock clock;

    public LoginAttemptService() {
        this(Clock.systemUTC());
    }

    LoginAttemptService(Clock clock) {
        this.clock = clock;
    }

    public void checkBlocked(String ip) {
        Instant now = clock.instant();
        Attempt attempt = attempts.get(ip);
        if (attempt != null && attempt.lockedUntil != null && now.isBefore(attempt.lockedUntil)) {
            long minutes = Math.max(1, Duration.between(now, attempt.lockedUntil).toMinutes() + 1);
            throw new TooManyRequestsException("로그인 실패가 많아 잠겼습니다. 약 " + minutes + "분 후 다시 시도하세요.");
        }
    }

    public void recordFailure(String ip) {
        if (attempts.size() > CLEANUP_THRESHOLD) {
            cleanup();
        }
        Instant now = clock.instant();
        attempts.compute(ip, (k, attempt) -> {
            if (attempt == null || attempt.isExpired(now)) {
                attempt = new Attempt(now);
            }
            attempt.failures++;
            if (attempt.failures >= MAX_FAILURES_PER_IP) {
                attempt.lockedUntil = now.plus(WINDOW);
            }
            return attempt;
        });
    }

    public void recordSuccess(String ip) {
        attempts.remove(ip);
    }

    private void cleanup() {
        Instant now = clock.instant();
        attempts.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
    }

    private static final class Attempt {
        private final Instant firstFailureAt;
        private int failures;
        private Instant lockedUntil;

        private Attempt(Instant firstFailureAt) {
            this.firstFailureAt = firstFailureAt;
        }

        private boolean isExpired(Instant now) {
            boolean windowPassed = !now.isBefore(firstFailureAt.plus(WINDOW));
            boolean unlocked = lockedUntil == null || !now.isBefore(lockedUntil);
            return windowPassed && unlocked;
        }
    }
}
