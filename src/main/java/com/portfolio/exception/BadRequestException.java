package com.portfolio.exception;

/** 잘못된 요청 (400) */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
