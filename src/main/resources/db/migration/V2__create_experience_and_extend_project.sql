-- =====================================================
-- V2: 경력 테이블 생성 + 프로젝트 테이블 확장
-- * V1 project 테이블은 기존 데이터를 유지하도록 ALTER 합니다.
--   (description 컬럼은 content(마크다운 본문)로 이름 변경)
-- * created_at / updated_at 은 JPA Auditing 이 채우고, DB 기본값은 안전장치입니다.
-- * 삭제는 물리 삭제 대신 is_deleted = 1 로 숨김 처리합니다.
-- =====================================================

-- -----------------------------------------------------
-- 경력
-- -----------------------------------------------------
CREATE TABLE experience
(
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    company_name     VARCHAR(100) NOT NULL COMMENT '회사명 (예: 클러쉬)',
    former_name      VARCHAR(100) COMMENT '이전 사명 (예: 디씨온)',
    department       VARCHAR(100) COMMENT '부서 (예: KB개발담당)',
    job_title        VARCHAR(100) COMMENT '담당 직무 (예: 프론트엔드 개발자)',
    role             VARCHAR(200) COMMENT '역할',
    position         VARCHAR(50) COMMENT '직급/직책',
    responsibilities TEXT COMMENT '담당 업무',
    start_date       DATE         NOT NULL COMMENT '입사일',
    end_date         DATE COMMENT '퇴사일 (NULL = 재직중)',
    is_deleted       TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '삭제 여부 (숨김 처리)',
    created_at       DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at       DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='경력';

-- -----------------------------------------------------
-- 프로젝트 (V1 테이블 확장)
-- -----------------------------------------------------
ALTER TABLE project
    RENAME COLUMN description TO content;

ALTER TABLE project
    MODIFY COLUMN title VARCHAR(200) NOT NULL COMMENT '프로젝트명',
    MODIFY COLUMN content MEDIUMTEXT COMMENT '내용 (마크다운)',
    ADD COLUMN experience_id    BIGINT COMMENT '소속 경력 (개인/사이드는 NULL)' AFTER id,
    ADD COLUMN summary          VARCHAR(300) COMMENT '한 줄 소개' AFTER title,
    ADD COLUMN role             VARCHAR(200) COMMENT '담당 역할' AFTER summary,
    ADD COLUMN responsibilities TEXT COMMENT '담당 업무' AFTER role,
    ADD COLUMN type             ENUM ('COMPANY','PERSONAL','SIDE') NOT NULL DEFAULT 'PERSONAL' COMMENT '종류 (회사/개인/사이드)' AFTER content,
    ADD COLUMN thumbnail_url    VARCHAR(500) COMMENT '썸네일' AFTER type,
    ADD COLUMN is_pinned        TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '상단 고정' AFTER thumbnail_url,
    ADD COLUMN sort_order       INT         NOT NULL DEFAULT 0 COMMENT '정렬 순서' AFTER is_pinned,
    ADD COLUMN start_date       DATE COMMENT '시작일' AFTER sort_order,
    ADD COLUMN end_date         DATE COMMENT '종료일 (NULL = 진행중)' AFTER start_date,
    ADD COLUMN is_deleted       TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '삭제 여부 (숨김 처리)' AFTER end_date,
    ADD CONSTRAINT fk_project_experience
        FOREIGN KEY (experience_id) REFERENCES experience (id) ON DELETE SET NULL;

ALTER TABLE project
    COMMENT ='프로젝트';
