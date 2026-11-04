package com.portfolio.auth;

import java.time.Instant;

public record IssuedToken(String token, Instant expiresAt) {
}
