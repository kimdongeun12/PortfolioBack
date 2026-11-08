package com.portfolio.domain.visit.repository;

public interface PageVisitStat {

    String getPath();

    Long getPageViews();

    Long getVisitors();

    Long getAvgDurationMs();

    Long getMeasured();
}
