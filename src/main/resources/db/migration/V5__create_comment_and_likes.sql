-- =====================================================
-- V5: 댓글(블로그·프로젝트 공용, 대댓글) + 좋아요(블로그·댓글)
-- =====================================================

-- -----------------------------------------------------
-- 댓글 (블로그·프로젝트 공용, 대댓글 1단계)
--  * target_id는 FK 불가 → 대상 존재 확인 / 글 삭제 시 정리는 Service에서 처리
--  * 대댓글의 target_type, target_id는 부모 댓글 값을 복사해서 저장
--  * 삭제는 소프트 삭제(is_deleted) → 대댓글은 유지, 화면에 "삭제된 댓글입니다"
-- -----------------------------------------------------
CREATE TABLE comment
(
    id          BIGINT                   NOT NULL AUTO_INCREMENT,
    target_type ENUM ('BLOG','PROJECT')  NOT NULL COMMENT '대상 종류',
    target_id   BIGINT                   NOT NULL COMMENT '대상 id (blog.id 또는 project.id)',
    parent_id   BIGINT COMMENT '부모 댓글 (NULL = 최상위 댓글)',
    nickname    VARCHAR(30)              NOT NULL COMMENT '닉네임',
    password    VARCHAR(100)             NOT NULL COMMENT '비밀번호 (BCrypt 해시)',
    content     TEXT                     NOT NULL COMMENT '내용',
    like_count  INT                      NOT NULL DEFAULT 0 COMMENT '좋아요 수 (likes 집계 캐시)',
    is_deleted  TINYINT(1)               NOT NULL DEFAULT 0 COMMENT '삭제 여부 (소프트 삭제)',
    created_at  DATETIME(6)              NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '작성일',
    updated_at  DATETIME(6)              NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    INDEX idx_comment_target (target_type, target_id, created_at),
    INDEX idx_comment_parent (parent_id),
    CONSTRAINT fk_comment_parent FOREIGN KEY (parent_id) REFERENCES comment (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='댓글';

-- -----------------------------------------------------
-- 좋아요 (블로그·댓글, IP 기준 중복 방지)
-- -----------------------------------------------------
CREATE TABLE likes
(
    id          BIGINT                  NOT NULL AUTO_INCREMENT,
    target_type ENUM ('BLOG','COMMENT') NOT NULL COMMENT '대상 종류',
    target_id   BIGINT                  NOT NULL COMMENT '대상 id (blog.id 또는 comment.id)',
    ip_hash     CHAR(64)                NOT NULL COMMENT 'IP SHA-256 해시',
    created_at  DATETIME(6)             NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_likes (target_type, target_id, ip_hash)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='좋아요';
