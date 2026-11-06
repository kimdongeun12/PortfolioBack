ALTER TABLE comment
    MODIFY COLUMN target_type ENUM ('BLOG','PROJECT','GUEST') NOT NULL COMMENT '대상 종류',
    ADD COLUMN edited_at DATETIME(6) COMMENT '내용 수정 시각 (NULL = 수정 안 됨)' AFTER is_deleted;
