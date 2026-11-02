package com.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

/**
 * 방문 통계 응답 DTO
 */
@Getter
@AllArgsConstructor
public class VisitStatsResponseDto {

    private final LocalDate from;
    private final LocalDate to;
    /** 기간 전체 페이지뷰 */
    private final long totalPageViews;
    /** 기간 전체 순 방문자 (IP 해시 기준) */
    private final long totalVisitors;
    private final long todayPageViews;
    private final long todayVisitors;
    /** 일별 통계 (방문 없는 날은 0 으로 채움) */
    private final List<Daily> daily;

    @Getter
    @AllArgsConstructor
    public static class Daily {
        private final LocalDate date;
        private final long pageViews;
        private final long visitors;
    }
}
