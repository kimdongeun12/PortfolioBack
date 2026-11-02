package com.portfolio.exception;

/** 인증 필요 (401) — 관리자 로그인이 안 되어 있거나 토큰이 만료됨 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
