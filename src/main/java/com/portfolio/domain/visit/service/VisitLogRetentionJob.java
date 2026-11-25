package com.portfolio.domain.visit.service;

import com.portfolio.domain.visit.repository.VisitLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 매일 새벽 4시에 어제까지의 방문 수를 일별 합계로 남긴 뒤, 1년이 지난 원본 방문 기록을 지운다.
 * 서버가 다시 시작될 때도 빠진 날짜의 합계를 채운다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class VisitLogRetentionJob {

    private static final int RETENTION_YEARS = 1;

    private final VisitService visitService;
    private final VisitLogRepository visitLogRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void summarizeOnStartup() {
        try {
            visitService.summarizeDaily();
        } catch (RuntimeException e) {
            log.warn("시작 시 방문 합계 집계 실패", e);
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul")
    public void deleteExpired() {
        visitService.summarizeDaily();
        int deleted = visitLogRepository.deleteOlderThan(LocalDateTime.now().minusYears(RETENTION_YEARS));
        if (deleted > 0) {
            log.info("보관 기간이 지난 방문 기록 {}건 삭제", deleted);
        }
    }
}
