package com.portfolio.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class AdminSessionResponseDto {

    private final boolean authenticated;
    private final Instant expiresAt;
}
