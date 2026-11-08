package com.portfolio.domain.visit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class VisitPageStatsResponseDto {

    private final LocalDate from;
    private final LocalDate to;
    private final Long avgDurationMs;
    private final List<Page> pages;

    @Getter
    @AllArgsConstructor
    public static class Page {
        private final String path;
        private final String label;
        private final long pageViews;
        private final long visitors;
        private final Long avgDurationMs;
        private final long measured;
    }
}
