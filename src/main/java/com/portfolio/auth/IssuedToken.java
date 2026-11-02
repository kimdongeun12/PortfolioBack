package com.portfolio.auth;

import java.time.Instant;

/**
 * 발급된 토큰과 만료 시각
 */
public record IssuedToken(String token, Instant expiresAt) {
}
