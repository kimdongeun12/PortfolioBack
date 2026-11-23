-- =====================================================
-- V18: User-Agent 원문 대신 기기·OS·브라우저 요약만 보관
-- =====================================================

ALTER TABLE visit_log
    ADD COLUMN device   VARCHAR(20) NULL COMMENT '기기 종류 (PC, 모바일, 태블릿)',
    ADD COLUMN os       VARCHAR(20) NULL COMMENT '운영체제',
    ADD COLUMN browser  VARCHAR(20) NULL COMMENT '브라우저',
    ADD COLUMN bot_like BOOLEAN     NOT NULL DEFAULT FALSE COMMENT '알려진 크롤러 표기가 있던 방문';

UPDATE visit_log
SET device   = CASE
                   WHEN user_agent IS NULL THEN NULL
                   WHEN user_agent LIKE BINARY '%iPad%' OR (user_agent LIKE BINARY '%Android%' AND user_agent NOT LIKE BINARY '%Mobile%') THEN '태블릿'
                   WHEN user_agent LIKE BINARY '%Mobi%' OR user_agent LIKE BINARY '%iPhone%' OR user_agent LIKE BINARY '%Android%' THEN '모바일'
                   ELSE 'PC'
               END,
    os       = CASE
                   WHEN user_agent IS NULL THEN NULL
                   WHEN user_agent LIKE BINARY '%iPhone%' OR user_agent LIKE BINARY '%iPad%' THEN 'iOS'
                   WHEN user_agent LIKE BINARY '%Android%' THEN 'Android'
                   WHEN user_agent LIKE BINARY '%Windows%' THEN 'Windows'
                   WHEN user_agent LIKE BINARY '%Mac OS X%' OR user_agent LIKE BINARY '%Macintosh%' THEN 'macOS'
                   WHEN user_agent LIKE BINARY '%Linux%' THEN 'Linux'
                   ELSE '기타'
               END,
    browser  = CASE
                   WHEN user_agent IS NULL THEN NULL
                   WHEN user_agent LIKE BINARY '%KAKAOTALK%' THEN '카카오톡'
                   WHEN user_agent LIKE BINARY '%NAVER(inapp%' THEN '네이버 앱'
                   WHEN user_agent LIKE BINARY '%Whale/%' THEN '웨일'
                   WHEN user_agent LIKE BINARY '%SamsungBrowser/%' THEN '삼성 인터넷'
                   WHEN user_agent LIKE BINARY '%Edg/%' OR user_agent LIKE BINARY '%EdgA/%' OR user_agent LIKE BINARY '%EdgiOS/%' THEN 'Edge'
                   WHEN user_agent LIKE BINARY '%Firefox/%' OR user_agent LIKE BINARY '%FxiOS/%' THEN 'Firefox'
                   WHEN user_agent LIKE BINARY '%CriOS/%' OR user_agent LIKE BINARY '%Chrome/%' THEN 'Chrome'
                   WHEN user_agent LIKE BINARY '%Safari/%' THEN 'Safari'
                   ELSE '기타'
               END,
    bot_like = (user_agent IS NOT NULL AND LOWER(user_agent) REGEXP
                'bot|crawler|spider|slurp|headless|preview|curl|wget|python|dataprovider|facebookexternalhit|yeti|daumoa|kakaotalk-scrap|lighthouse|inspectiontool|go-http-client|okhttp|java/|node-fetch|axios|scrapy|claude/|googleother|google-safety|mediapartners-google|apis-google|feedfetcher-google|google-read-aloud');

ALTER TABLE visit_log DROP COLUMN user_agent;
