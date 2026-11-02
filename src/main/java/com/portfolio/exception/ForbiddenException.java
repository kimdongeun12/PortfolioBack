package com.portfolio.exception;

/** 권한 없음 (403) — 예: 댓글 비밀번호 불일치 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
