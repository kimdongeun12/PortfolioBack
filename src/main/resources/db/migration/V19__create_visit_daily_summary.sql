-- =====================================================
-- V19: 일별 방문 합계 (원본 방문 기록이 지워진 뒤에도 누적 방문자 유지)
-- =====================================================

CREATE TABLE visit_daily_summary
(
    visit_date DATE        NOT NULL COMMENT '방문 날짜',
    page_views INT         NOT NULL COMMENT '그날 페이지뷰 (크롤러 제외)',
    visitors   INT         NOT NULL COMMENT '그날 방문자 수 (IP 해시 기준, 크롤러 제외)',
    updated_at DATETIME(6) NOT NULL COMMENT '마지막 집계 시각',
    PRIMARY KEY (visit_date)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='일별 방문 합계';

INSERT INTO visit_daily_summary (visit_date, page_views, visitors, updated_at)
SELECT DATE(visited_at), COUNT(*), COUNT(DISTINCT ip_hash), NOW(6)
FROM visit_log
WHERE bot_like = FALSE
GROUP BY DATE(visited_at);
