package com.portfolio.auth;

import com.portfolio.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

@Service
public class AdminAuthService {

    static final String ADMIN_SUBJECT = "admin";

    private final String adminPassword;
    private final JwtProvider jwtProvider;
    private final LoginAttemptService loginAttemptService;

    public AdminAuthService(@Value("${app.admin.password}") String adminPassword,
                            JwtProvider jwtProvider,
                            LoginAttemptService loginAttemptService) {
        this.adminPassword = adminPassword;
        this.jwtProvider = jwtProvider;
        this.loginAttemptService = loginAttemptService;
    }

    public IssuedToken login(String password, String clientIp) {
        loginAttemptService.checkBlocked(clientIp);

        if (adminPassword == null || adminPassword.isBlank()) {
            throw new UnauthorizedException("관리자 비밀번호(ADMIN_PASSWORD)가 설정되지 않아 로그인할 수 없습니다.");
        }
        if (!matches(password, adminPassword)) {
            loginAttemptService.recordFailure(clientIp);
            throw new UnauthorizedException("비밀번호가 올바르지 않습니다.");
        }

        loginAttemptService.recordSuccess(clientIp);
        return jwtProvider.createToken(ADMIN_SUBJECT);
    }

    public boolean isAuthenticated(String token) {
        return jwtProvider.isValid(token, ADMIN_SUBJECT);
    }

    public Duration getTokenValidity() {
        return jwtProvider.getValidity();
    }

    private boolean matches(String input, String expected) {
        if (input == null) {
            return false;
        }
        return MessageDigest.isEqual(sha256(input), sha256(expected));
    }

    private byte[] sha256(String value) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
