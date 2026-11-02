-- =====================================================
-- V7: 방문 통계
-- =====================================================

-- -----------------------------------------------------
-- 방문 통계
-- -----------------------------------------------------
CREATE TABLE visit_log
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    session_id VARCHAR(36)  NOT NULL COMMENT '방문 키 (브라우저 세션 UUID)',
    path       VARCHAR(500) NOT NULL COMMENT '방문 경로 (예: /blog/12)',
    referrer   VARCHAR(500) COMMENT '유입 경로',
    user_agent VARCHAR(500) COMMENT 'User-Agent',
    ip_hash    CHAR(64) COMMENT 'IP SHA-256 해시',
    visited_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '방문일시',
    PRIMARY KEY (id),
    INDEX idx_visit_visited_at (visited_at),
    INDEX idx_visit_path (path)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='방문 통계';
