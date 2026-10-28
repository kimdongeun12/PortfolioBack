-- 참고용 SQL (자동 실행되지 않습니다)
-- ddl-auto: update 설정으로 테이블은 앱 실행 시 자동 생성됩니다.
-- 처음 한 번 DB만 만들어 주세요.

CREATE DATABASE IF NOT EXISTS portfolio DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 자동 생성되는 테이블 구조 (참고)
-- CREATE TABLE project (
--     id          BIGINT AUTO_INCREMENT PRIMARY KEY,
--     title       VARCHAR(100) NOT NULL,
--     description TEXT,
--     created_at  DATETIME(6),
--     updated_at  DATETIME(6)
-- );
