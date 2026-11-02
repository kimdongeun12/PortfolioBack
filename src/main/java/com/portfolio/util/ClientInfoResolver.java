package com.portfolio.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.regex.Pattern;

/**
 * 요청에서 클라이언트 정보(IP, User-Agent)를 꺼냅니다.
 * IP 는 원문을 저장하지 않고 salt + SHA-256 해시로만 저장합니다.
 */
@Component
public class ClientInfoResolver {

    private static final Pattern BOT_PATTERN =
            Pattern.compile(".*(bot|crawler|spider|slurp|headless|preview|curl|wget|python-requests).*",
                    Pattern.CASE_INSENSITIVE);

    private final String salt;

    public ClientInfoResolver(@Value("${app.ip-hash-salt}") String salt) {
        this.salt = salt;
    }

    /** 프록시(Nginx, 로드밸런서) 뒤에서도 실제 사용자 IP 를 꺼냅니다. */
    public String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }

    /** 요청 IP 의 해시값 (64자) */
    public String getIpHash(HttpServletRequest request) {
        return sha256(salt + getClientIp(request));
    }

    /** User-Agent (DB 컬럼 길이 500자에 맞춰 자름) */
    public String getUserAgent(HttpServletRequest request) {
        return truncate(request.getHeader("User-Agent"), 500);
    }

    /** 검색엔진 봇 / 스크립트 요청 여부 */
    public boolean isBot(String userAgent) {
        return userAgent == null || userAgent.isBlank() || BOT_PATTERN.matcher(userAgent).matches();
    }

    public static String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }

    private String sha256(String value) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 을 사용할 수 없습니다.", e);
        }
    }
}
