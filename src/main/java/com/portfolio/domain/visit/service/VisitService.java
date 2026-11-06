package com.portfolio.domain.visit.service;

import com.portfolio.domain.visit.dto.VisitRequestDto;
import com.portfolio.domain.visit.dto.VisitStatsResponseDto;

import java.time.LocalDate;

public interface VisitService {

    void record(VisitRequestDto requestDto, String ipHash, String userAgent);

    VisitStatsResponseDto getStats(LocalDate from, LocalDate to);
}
