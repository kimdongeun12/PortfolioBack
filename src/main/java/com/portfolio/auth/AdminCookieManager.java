package com.portfolio.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

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
