-- =====================================================
-- V3: 기술 스택 + 프로젝트-기술 연결
-- =====================================================

-- -----------------------------------------------------
-- 기술 스택 (프로젝트·블로그 공용)
-- -----------------------------------------------------
CREATE TABLE tech_stack
(
    id   BIGINT      NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '기술명 (예: React, Vue, TypeScript)',
    PRIMARY KEY (id),
    UNIQUE KEY uk_tech_stack_name (name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='기술 스택';

CREATE TABLE project_tech
(
    project_id BIGINT NOT NULL,
    tech_id    BIGINT NOT NULL,
    PRIMARY KEY (project_id, tech_id),
    CONSTRAINT fk_project_tech_project FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE,
    CONSTRAINT fk_project_tech_tech FOREIGN KEY (tech_id) REFERENCES tech_stack (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='프로젝트-기술 연결';
