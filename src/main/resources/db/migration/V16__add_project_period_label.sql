-- =====================================================
-- V16: 프로젝트 기간 표기 직접 지정
-- =====================================================

ALTER TABLE project
    ADD COLUMN period_label VARCHAR(40) NULL COMMENT '기간 표기 (비우면 시작·종료일로 표시)' AFTER end_date;
