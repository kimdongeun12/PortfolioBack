package com.portfolio.domain.visit.service;

import com.portfolio.domain.visit.dto.VisitRequestDto;
import com.portfolio.domain.visit.dto.VisitStatsResponseDto;
import com.portfolio.domain.visit.entity.VisitLog;
import com.portfolio.domain.visit.repository.DailyVisitStat;
import com.portfolio.domain.visit.repository.VisitLogRepository;
import com.portfolio.global.exception.BadRequestException;
import com.portfolio.global.util.ClientInfoResolver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitServiceImpl implements VisitService {

    private static final long DUPLICATE_WINDOW_SECONDS = 10;
    private static final long MAX_STATS_DAYS = 366;

    private final VisitLogRepository visitLogRepository;
    private final ClientInfoResolver clientInfoResolver;

    @Override
    @Transactional
    public void record(VisitRequestDto requestDto, String ipHash, String userAgent) {
        if (clientInfoResolver.isBot(userAgent)) {
            return;
        }

        String path = ClientInfoResolver.truncate(requestDto.getPath().trim(), 500);
        LocalDateTime threshold = LocalDateTime.now().minusSeconds(DUPLICATE_WINDOW_SECONDS);
        if (visitLogRepository.existsBySessionIdAndPathAndVisitedAtAfter(requestDto.getSessionId(), path, threshold)) {
            return;
        }

        VisitLog visitLog = VisitLog.builder()
                .sessionId(requestDto.getSessionId())
                .path(path)
                .referrer(blankToNull(ClientInfoResolver.truncate(requestDto.getReferrer(), 500)))
                .userAgent(userAgent)
                .ipHash(ipHash)
                .build();
        visitLogRepository.save(visitLog);
    }

    @Override
    public VisitStatsResponseDto getStats(LocalDate from, LocalDate to) {
        if (to.isBefore(from)) {
            throw new BadRequestException("종료일은 시작일보다 빠를 수 없습니다.");
        }
        if (ChronoUnit.DAYS.between(from, to) >= MAX_STATS_DAYS) {
            throw new BadRequestException("통계 조회 기간은 최대 1년입니다.");
        }

        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.plusDays(1).atStartOfDay();

        Map<LocalDate, DailyVisitStat> statByDate = new HashMap<>();
        for (DailyVisitStat stat : visitLogRepository.findDailyStats(start, end)) {
            statByDate.put(LocalDate.parse(stat.getDay()), stat);
        }
        List<VisitStatsResponseDto.Daily> daily = new ArrayList<>();
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            DailyVisitStat stat = statByDate.get(date);
            daily.add(new VisitStatsResponseDto.Daily(
                    date,
                    stat != null ? stat.getPageViews() : 0L,
                    stat != null ? stat.getVisitors() : 0L
            ));
        }

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = todayStart.plusDays(1);

        return new VisitStatsResponseDto(
                from,
                to,
                visitLogRepository.countPageViews(start, end),
                visitLogRepository.countVisitors(start, end),
                visitLogRepository.countPageViews(todayStart, todayEnd),
                visitLogRepository.countVisitors(todayStart, todayEnd),
                daily
        );
    }

    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }
}
