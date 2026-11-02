package com.portfolio.repository;

/**
 * 일별 방문 통계 조회 결과 (native query 의 컬럼 별칭과 getter 이름이 같아야 합니다)
 */
public interface DailyVisitStat {

    /** "2026-11-02" 형식 */
    String getDay();

    Long getPageViews();

    Long getVisitors();
}
