package com.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class VisitStatsResponseDto {

    private final LocalDate from;
    private final LocalDate to;
    private final long totalPageViews;
    private final long totalVisitors;
    private final long todayPageViews;
    private final long todayVisitors;
    private final List<Daily> daily;

    @Getter
    @AllArgsConstructor
    public static class Daily {
        private final LocalDate date;
        private final long pageViews;
        private final long visitors;
    }
}
