package com.portfolio.repository;

import com.portfolio.domain.VisitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {

    /** 같은 세션이 같은 경로를 짧은 시간 안에 다시 보냈는지 (새로고침 중복 방지) */
    boolean existsBySessionIdAndPathAndVisitedAtAfter(String sessionId, String path, LocalDateTime after);

    /** 일별 페이지뷰 / 순 방문자(IP 해시 기준) — [from, to) 구간 */
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
