-- =====================================================
-- V8: 관리자 댓글 구분
-- * 어드민에서 작성한 댓글은 is_admin = 1 (화면에 '작성자' 배지 표시)
-- * 관리자 댓글은 비밀번호 없이 작성하며, password 에는 사용할 수 없는 임의 해시를 저장합니다.
-- =====================================================
ALTER TABLE comment
    ADD COLUMN is_admin TINYINT(1) NOT NULL DEFAULT 0 COMMENT '관리자 작성 여부' AFTER content;
