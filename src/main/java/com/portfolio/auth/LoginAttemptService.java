package com.portfolio.auth;

import com.portfolio.exception.TooManyRequestsException;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 로그인 실패 횟수 제한 (비밀번호 무작위 대입 방지, 메모리 저장)
 *
 * - IP 별: 15분 안에 5번 틀리면 15분 잠금
 * - 전체: 15분 안에 20번 틀리면 15분 잠금
 *   (X-Forwarded-For 헤더를 바꿔가며 IP 제한을 우회하는 경우까지 막기 위함)
 */
@Component
public class LoginAttemptService {

    static final int MAX_FAILURES_PER_IP = 5;
    static final int MAX_FAILURES_GLOBAL = 20;
    static final Duration WINDOW = Duration.ofMinutes(15);
    static final String GLOBAL_KEY = "__global__";
    private static final int CLEANUP_THRESHOLD = 10_000;

    private final Map<String, Attempt> attempts = new ConcurrentHashMap<>();
    private final Clock clock;

    public LoginAttemptService() {
        this(Clock.systemUTC());
    }

    LoginAttemptService(Clock clock) {
        this.clock = clock;
    }

    /** 잠겨 있으면 TooManyRequestsException */
    public void checkBlocked(String ip) {
        Instant now = clock.instant();
        for (String key : new String[]{ip, GLOBAL_KEY}) {
            Attempt attempt = attempts.get(key);
            if (attempt != null && attempt.lockedUntil != null && now.isBefore(attempt.lockedUntil)) {
                long minutes = Math.max(1, Duration.between(now, attempt.lockedUntil).toMinutes() + 1);
                throw new TooManyRequestsException("로그인 실패가 많아 잠겼습니다. 약 " + minutes + "분 후 다시 시도하세요.");
            }
        }
    }

    public void recordFailure(String ip) {
        if (attempts.size() > CLEANUP_THRESHOLD) {
            cleanup();
        }
        record(ip, MAX_FAILURES_PER_IP);
        record(GLOBAL_KEY, MAX_FAILURES_GLOBAL);
    }

    /** 로그인 성공 시 해당 IP 기록 초기화 */
    public void recordSuccess(String ip) {
        attempts.remove(ip);
    }

    private void record(String key, int maxFailures) {
        Instant now = clock.instant();
        attempts.compute(key, (k, attempt) -> {
            if (attempt == null || attempt.isExpired(now)) {
                attempt = new Attempt(now);
            }
            attempt.failures++;
            if (attempt.failures >= maxFailures) {
                attempt.lockedUntil = now.plus(WINDOW);
            }
            return attempt;
        });
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

        /** 기록 구간이 지났고 잠금도 풀렸으면 새로 셉니다. */
        private boolean isExpired(Instant now) {
            boolean windowPassed = !now.isBefore(firstFailureAt.plus(WINDOW));
            boolean unlocked = lockedUntil == null || !now.isBefore(lockedUntil);
            return windowPassed && unlocked;
        }
    }
}
