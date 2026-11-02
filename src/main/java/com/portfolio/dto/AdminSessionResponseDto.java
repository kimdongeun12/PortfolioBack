package com.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

/**
 * 로그인 결과 / 로그인 상태 응답
 */
@Getter
@AllArgsConstructor
public class AdminSessionResponseDto {

    private final boolean authenticated;
    /** 토큰 만료 시각 (로그인 응답에만 포함, /me 에서는 null) */
    private final Instant expiresAt;
}
