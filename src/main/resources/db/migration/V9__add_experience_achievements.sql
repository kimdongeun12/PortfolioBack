ALTER TABLE experience
    MODIFY COLUMN responsibilities TEXT COMMENT '주요 업무',
    ADD COLUMN achievements TEXT COMMENT '주요 성과' AFTER responsibilities;
