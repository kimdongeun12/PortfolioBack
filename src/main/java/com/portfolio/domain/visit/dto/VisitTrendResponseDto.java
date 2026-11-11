package com.portfolio.domain.visit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class VisitTrendResponseDto {

    private final LocalDate from;
    private final LocalDate to;
    private final String unit;
    private final String path;
    private final List<Period> periods;

    @Getter
    @AllArgsConstructor
    public static class Period {
        private final String period;
        private final long pageViews;
        private final long visitors;
        private final Long avgDurationMs;
        private final long measured;
    }
}
