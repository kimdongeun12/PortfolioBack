CREATE TABLE skill_group
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(50)  NOT NULL COMMENT '그룹명',
    description VARCHAR(300) COMMENT '설명',
    sort_order  INT          NOT NULL DEFAULT 0 COMMENT '정렬 순서',
    is_deleted  TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '삭제 여부 (숨김 처리)',
    created_at  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='스킬 그룹';

CREATE TABLE skill_group_tech
(
    skill_group_id BIGINT NOT NULL,
    sort_order     INT    NOT NULL,
    tech_id        BIGINT NOT NULL,
    PRIMARY KEY (skill_group_id, sort_order),
    CONSTRAINT fk_skill_group_tech_group FOREIGN KEY (skill_group_id) REFERENCES skill_group (id) ON DELETE CASCADE,
    CONSTRAINT fk_skill_group_tech_tech FOREIGN KEY (tech_id) REFERENCES tech_stack (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='스킬 그룹-기술 연결';
