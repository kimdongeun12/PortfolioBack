package com.portfolio.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 관리자 토큰 쿠키 생성·읽기
 * - HttpOnly: 자바스크립트에서 읽을 수 없음 (XSS 로 토큰 탈취 방지)
 * - Secure: HTTPS 에서만 전송 (운영에서 true)
 * - SameSite: 다른 사이트에서 보낸 요청에 쿠키가 붙지 않게 함 (기본 Lax)
 */
@Component
public class AdminCookieManager {

    public static final String COOKIE_NAME = "admin_token";

    private final boolean secure;
    private final String sameSite;

    public AdminCookieManager(@Value("${app.admin.cookie-secure}") boolean secure,
                              @Value("${app.admin.cookie-same-site}") String sameSite) {
        this.secure = secure;
        this.sameSite = sameSite;
    }

    public ResponseCookie createLoginCookie(String token, Duration maxAge) {
        return baseCookie(token).maxAge(maxAge).build();
    }

    public ResponseCookie createLogoutCookie() {
        return baseCookie("").maxAge(0).build();
    }

    /** 요청 쿠키에서 토큰 꺼내기 (없으면 null) */
    public String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private ResponseCookie.ResponseCookieBuilder baseCookie(String value) {
        return ResponseCookie.from(COOKIE_NAME, value)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path("/");
    }
}
