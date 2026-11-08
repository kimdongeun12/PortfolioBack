package com.portfolio.domain.visit.repository;

import com.portfolio.domain.visit.entity.VisitLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {

    Optional<VisitLog> findTopBySessionIdAndPathAndVisitedAtAfterOrderByIdDesc(String sessionId, String path, LocalDateTime after);

    long countByPath(String path);

    @Query(value = """
            SELECT path                                         AS path,
                   COUNT(*)                                     AS pageViews,
                   COUNT(DISTINCT ip_hash)                      AS visitors,
                   CAST(AVG(duration_ms) AS SIGNED)             AS avgDurationMs,
                   SUM(CASE WHEN duration_ms IS NULL THEN 0 ELSE 1 END) AS measured
            FROM visit_log
            WHERE visited_at >= :from AND visited_at < :to
            GROUP BY path
            ORDER BY pageViews DESC
            LIMIT 300
            """, nativeQuery = true)
    List<PageVisitStat> findPageStats(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query(value = "SELECT CAST(AVG(duration_ms) AS SIGNED) FROM visit_log WHERE visited_at >= :from AND visited_at < :to AND duration_ms IS NOT NULL",
            nativeQuery = true)
    Long averageDuration(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query(value = """
            SELECT DATE_FORMAT(visited_at, '%Y-%m-%d') AS day,
                   COUNT(*)                            AS pageViews,
                   COUNT(DISTINCT ip_hash)             AS visitors
            FROM visit_log
            WHERE visited_at >= :from AND visited_at < :to
            GROUP BY DATE_FORMAT(visited_at, '%Y-%m-%d')
            ORDER BY day
            """, nativeQuery = true)
    List<DailyVisitStat> findDailyStats(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query(value = "SELECT COUNT(*) FROM visit_log WHERE visited_at >= :from AND visited_at < :to",
            nativeQuery = true)
    long countPageViews(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query(value = "SELECT COUNT(DISTINCT ip_hash) FROM visit_log WHERE visited_at >= :from AND visited_at < :to",
            nativeQuery = true)
    long countVisitors(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
