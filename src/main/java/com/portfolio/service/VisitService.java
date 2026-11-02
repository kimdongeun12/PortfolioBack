package com.portfolio.service;

import com.portfolio.dto.VisitRequestDto;
import com.portfolio.dto.VisitStatsResponseDto;

import java.time.LocalDate;

/**
 * 방문 기록 비즈니스 로직 인터페이스
 */
public interface VisitService {

    /** 방문 기록 저장 (봇·새로고침 중복은 저장하지 않음) */
    void record(VisitRequestDto requestDto, String ipHash, String userAgent);

    /** 기간별 통계 (from ~ to, 양 끝 날짜 포함) */
    VisitStatsResponseDto getStats(LocalDate from, LocalDate to);
}
