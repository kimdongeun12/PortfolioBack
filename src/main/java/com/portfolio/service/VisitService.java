package com.portfolio.service;

import com.portfolio.dto.VisitRequestDto;
import com.portfolio.dto.VisitStatsResponseDto;

import java.time.LocalDate;

public interface VisitService {

    void record(VisitRequestDto requestDto, String ipHash, String userAgent);

    VisitStatsResponseDto getStats(LocalDate from, LocalDate to);
}
