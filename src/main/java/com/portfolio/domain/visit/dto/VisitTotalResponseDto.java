package com.portfolio.domain.visit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VisitTotalResponseDto {

    private final long totalPageViews;
    private final long totalVisitors;
    private final long todayVisitors;
}
