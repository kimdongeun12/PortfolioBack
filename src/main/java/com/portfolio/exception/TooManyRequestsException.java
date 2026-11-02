package com.portfolio.exception;

/** 요청 과다 (429) — 로그인 실패가 너무 많아 잠시 잠김 */
public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String message) {
        super(message);
    }
}
