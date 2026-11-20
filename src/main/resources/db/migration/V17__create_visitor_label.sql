-- =====================================================
-- V17: 방문자 이름표 (내 기기 등 구분용)
-- =====================================================

CREATE TABLE visitor_label (
    ip_hash    CHAR(64)    NOT NULL PRIMARY KEY COMMENT '방문 기록의 IP 해시',
    label      VARCHAR(40) NOT NULL COMMENT '관리자가 붙인 이름 (예: 나 - 집)',
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
