package com.portfolio.domain.visit.repository;

import com.portfolio.domain.visit.entity.VisitDailySummary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface VisitDailySummaryRepository extends JpaRepository<VisitDailySummary, LocalDate> {

    @Modifying
    @Query(value = """
            INSERT INTO visit_daily_summary (visit_date, page_views, visitors, updated_at)
            SELECT DATE(visited_at), COUNT(*), COUNT(DISTINCT ip_hash), :now
            FROM visit_log
            WHERE visited_at >= :from AND visited_at < :to AND bot_like = FALSE
            GROUP BY DATE(visited_at)
            ON DUPLICATE KEY UPDATE page_views = VALUES(page_views),
                                    visitors   = VALUES(visitors),
                                    updated_at = VALUES(updated_at)
            """, nativeQuery = true)
    int upsertRange(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("now") LocalDateTime now);

    @Query("select max(s.visitDate) from VisitDailySummary s where s.visitDate < :today")
    LocalDate findLastSummarizedBefore(@Param("today") LocalDate today);

    @Query("select coalesce(sum(s.pageViews), 0) from VisitDailySummary s where s.visitDate < :today")
    long sumPageViewsBefore(@Param("today") LocalDate today);

    @Query("select coalesce(sum(s.visitors), 0) from VisitDailySummary s where s.visitDate < :today")
    long sumVisitorsBefore(@Param("today") LocalDate today);
}
