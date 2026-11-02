-- =====================================================
-- V4: 블로그 + 블로그-기술 연결
-- =====================================================

-- -----------------------------------------------------
-- 블로그
-- -----------------------------------------------------
CREATE TABLE blog
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    title         VARCHAR(200) NOT NULL COMMENT '제목',
    content       MEDIUMTEXT   NOT NULL COMMENT '내용 (마크다운)',
    thumbnail_url VARCHAR(500) COMMENT '썸네일',
    like_count    INT          NOT NULL DEFAULT 0 COMMENT '좋아요 수 (likes 집계 캐시)',
    is_published  TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '공개 여부 (0 = 임시저장)',
    is_deleted    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '삭제 여부 (숨김 처리)',
    created_at    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '작성일',
    updated_at    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    INDEX idx_blog_list (is_deleted, is_published, created_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='블로그';

CREATE TABLE blog_tech
(
    blog_id BIGINT NOT NULL,
    tech_id BIGINT NOT NULL,
    PRIMARY KEY (blog_id, tech_id),
    CONSTRAINT fk_blog_tech_blog FOREIGN KEY (blog_id) REFERENCES blog (id) ON DELETE CASCADE,
    CONSTRAINT fk_blog_tech_tech FOREIGN KEY (tech_id) REFERENCES tech_stack (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='블로그-기술 연결';
