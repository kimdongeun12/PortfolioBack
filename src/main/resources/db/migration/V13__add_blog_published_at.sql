-- =====================================================
-- V13: 블로그 게시일 (원래 작성일을 지정할 수 있도록 작성 시각과 분리)
-- =====================================================

ALTER TABLE blog
    ADD COLUMN published_at DATETIME(6) NULL COMMENT '게시일' AFTER is_deleted;

UPDATE blog SET published_at = created_at;

ALTER TABLE blog
    MODIFY published_at DATETIME(6) NOT NULL COMMENT '게시일';

CREATE INDEX idx_blog_published ON blog (is_deleted, is_published, published_at);
