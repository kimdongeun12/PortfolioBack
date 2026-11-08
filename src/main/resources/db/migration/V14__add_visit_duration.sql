-- =====================================================
-- V14: 방문 기록에 체류 시간 추가
-- =====================================================

ALTER TABLE visit_log
    ADD COLUMN duration_ms INT NULL COMMENT '체류 시간 (ms, 화면이 보이는 동안만 측정)' AFTER ip_hash;
