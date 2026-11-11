package com.portfolio.domain.visit.repository;

public interface PeriodVisitStat {

    String getPeriod();

    Long getPageViews();

    Long getVisitors();

    Long getAvgDurationMs();

    Long getMeasured();
}
