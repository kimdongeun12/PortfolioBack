package com.portfolio.repository;

import com.portfolio.domain.VisitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {

    boolean existsBySessionIdAndPathAndVisitedAtAfter(String sessionId, String path, LocalDateTime after);

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
