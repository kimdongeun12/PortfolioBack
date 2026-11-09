-- =====================================================
-- V15: 프로젝트 타이포 커버 문구 추가
-- =====================================================

ALTER TABLE project
    ADD COLUMN cover_label VARCHAR(40)  NULL COMMENT '커버 분야 라벨' AFTER thumbnail_url,
    ADD COLUMN cover_title VARCHAR(60)  NULL COMMENT '커버 큰 제목' AFTER cover_label,
    ADD COLUMN cover_sub   VARCHAR(100) NULL COMMENT '커버 보조 문구' AFTER cover_title;
