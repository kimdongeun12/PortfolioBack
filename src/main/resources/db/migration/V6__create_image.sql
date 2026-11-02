-- =====================================================
-- V6: 이미지
-- =====================================================

-- -----------------------------------------------------
-- 이미지 (파일은 디스크/S3, DB에는 경로만)
-- -----------------------------------------------------
CREATE TABLE image
(
    id            BIGINT                  NOT NULL AUTO_INCREMENT,
    target_type   ENUM ('PROJECT','BLOG') NOT NULL COMMENT '대상 종류',
    target_id     BIGINT                  NOT NULL COMMENT '대상 id',
    url           VARCHAR(500)            NOT NULL COMMENT '저장 경로 / URL',
    original_name VARCHAR(255) COMMENT '원본 파일명',
    mime_type     VARCHAR(50) COMMENT 'MIME 타입',
    file_size     BIGINT COMMENT '파일 크기 (byte)',
    is_deleted    TINYINT(1)              NOT NULL DEFAULT 0 COMMENT '삭제 여부 (숨김 처리)',
    created_at    DATETIME(6)             NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    INDEX idx_image_target (target_type, target_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='이미지';
