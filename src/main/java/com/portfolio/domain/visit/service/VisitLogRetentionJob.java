package com.portfolio.domain.visit.service;

import com.portfolio.domain.visit.repository.VisitLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 방문 기록은 1년만 보관한다. 매일 새벽 4시에 1년이 지난 기록을 지운다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class VisitLogRetentionJob {

    private static final int RETENTION_YEARS = 1;

    private final VisitLogRepository visitLogRepository;

    @Transactional
    @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul")
    public void deleteExpired() {
        int deleted = visitLogRepository.deleteOlderThan(LocalDateTime.now().minusYears(RETENTION_YEARS));
        if (deleted > 0) {
            log.info("보관 기간이 지난 방문 기록 {}건 삭제", deleted);
        }
    }
}
