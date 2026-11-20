package com.portfolio.domain.visit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class VisitorTimelineResponseDto {

    private final LocalDate from;
    private final LocalDate to;
    private final boolean truncated;
    private final List<Visitor> visitors;

    @Getter
    @AllArgsConstructor
    public static class Visitor {
        private final String visitorId;
        private final String ipHash;
        private final String label;
        private final String device;
        private final String browser;
        private final String os;
        private final String firstReferrer;
        private final LocalDateTime firstVisitedAt;
        private final LocalDateTime lastVisitedAt;
        private final long pageViews;
        private final long sessions;
        private final long totalDurationMs;
        private final List<Visit> visits;
    }

    @Getter
    @AllArgsConstructor
    public static class Visit {
        private final LocalDateTime visitedAt;
        private final String path;
        private final String label;
        private final Integer durationMs;
        private final String referrer;
        private final String sessionId;
    }
}
