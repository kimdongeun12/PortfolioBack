package com.portfolio.domain.visit.service;

import com.portfolio.domain.visit.dto.VisitRequestDto;
import com.portfolio.domain.visit.dto.VisitPageStatsResponseDto;
import com.portfolio.domain.visit.dto.VisitStatsResponseDto;
import com.portfolio.domain.visit.dto.VisitTrendResponseDto;

import java.time.LocalDate;

public interface VisitService {

    Long record(VisitRequestDto requestDto, String ipHash, String userAgent);

    void updateDuration(Long id, String sessionId, int durationMs);

    long countPageViews(String path);

    VisitPageStatsResponseDto getPageStats(LocalDate from, LocalDate to);

    VisitStatsResponseDto getStats(LocalDate from, LocalDate to);

    VisitTrendResponseDto getTrend(LocalDate from, LocalDate to, String unit, String path);
}
