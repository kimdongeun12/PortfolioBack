package com.portfolio.domain.visit.service;

import com.portfolio.domain.blog.entity.Blog;
import com.portfolio.domain.blog.repository.BlogRepository;
import com.portfolio.domain.project.entity.Project;
import com.portfolio.domain.project.repository.ProjectRepository;
import com.portfolio.domain.visit.dto.VisitPageStatsResponseDto;
import com.portfolio.domain.visit.dto.VisitRequestDto;
import com.portfolio.domain.visit.dto.VisitStatsResponseDto;
import com.portfolio.domain.visit.entity.VisitLog;
import com.portfolio.domain.visit.repository.DailyVisitStat;
import com.portfolio.domain.visit.repository.PageVisitStat;
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
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisitServiceImpl implements VisitService {

    private static final long DUPLICATE_WINDOW_SECONDS = 10;
    private static final long MAX_STATS_DAYS = 366;
    private static final int MAX_DURATION_MS = 30 * 60 * 1000;
    private static final long DURATION_UPDATE_HOURS = 6;
    private static final Pattern DETAIL_PATH = Pattern.compile("^/(blog|portfolio)/(\\d+)$");
    private static final Map<String, String> PAGE_LABELS = Map.of(
            "/", "프로필",
            "/blog", "블로그 목록",
            "/portfolio", "포트폴리오 목록",
            "/guest", "방명록"
    );

    private final VisitLogRepository visitLogRepository;
    private final ClientInfoResolver clientInfoResolver;
    private final BlogRepository blogRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public Long record(VisitRequestDto requestDto, String ipHash, String userAgent) {
        if (clientInfoResolver.isBot(userAgent)) {
            return null;
        }

        String path = ClientInfoResolver.truncate(requestDto.getPath().trim(), 500);
        LocalDateTime threshold = LocalDateTime.now().minusSeconds(DUPLICATE_WINDOW_SECONDS);
        var recent = visitLogRepository.findTopBySessionIdAndPathAndVisitedAtAfterOrderByIdDesc(requestDto.getSessionId(), path, threshold);
        if (recent.isPresent()) {
            return recent.get().getId();
        }

        VisitLog visitLog = VisitLog.builder()
                .sessionId(requestDto.getSessionId())
                .path(path)
                .referrer(blankToNull(ClientInfoResolver.truncate(requestDto.getReferrer(), 500)))
                .userAgent(userAgent)
                .ipHash(ipHash)
                .build();
        return visitLogRepository.save(visitLog).getId();
    }

    @Override
    @Transactional
    public void updateDuration(Long id, String sessionId, int durationMs) {
        if (id == null || sessionId == null || durationMs <= 0) {
            return;
        }
        visitLogRepository.findById(id)
                .filter(log -> log.getSessionId().equals(sessionId))
                .filter(log -> log.getVisitedAt() != null
                        && log.getVisitedAt().isAfter(LocalDateTime.now().minusHours(DURATION_UPDATE_HOURS)))
                .ifPresent(log -> log.updateDuration(Math.min(durationMs, MAX_DURATION_MS)));
    }

    @Override
    public long countPageViews(String path) {
        return visitLogRepository.countByPath(path);
    }

    @Override
    public VisitPageStatsResponseDto getPageStats(LocalDate from, LocalDate to) {
        validateRange(from, to);
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.plusDays(1).atStartOfDay();

        List<PageVisitStat> stats = visitLogRepository.findPageStats(start, end);
        Set<Long> blogIds = new HashSet<>();
        Set<Long> projectIds = new HashSet<>();
        for (PageVisitStat stat : stats) {
            Matcher matcher = DETAIL_PATH.matcher(stat.getPath());
            if (matcher.matches()) {
                (matcher.group(1).equals("blog") ? blogIds : projectIds).add(Long.parseLong(matcher.group(2)));
            }
        }
        Map<Long, String> blogTitles = new HashMap<>();
        for (Blog blog : blogRepository.findAllById(blogIds)) {
            blogTitles.put(blog.getId(), blog.getTitle());
        }
        Map<Long, String> projectTitles = new HashMap<>();
        for (Project project : projectRepository.findAllById(projectIds)) {
            projectTitles.put(project.getId(), project.getTitle());
        }

        List<VisitPageStatsResponseDto.Page> pages = new ArrayList<>();
        for (PageVisitStat stat : stats) {
            pages.add(new VisitPageStatsResponseDto.Page(
                    stat.getPath(),
                    toLabel(stat.getPath(), blogTitles, projectTitles),
                    stat.getPageViews() != null ? stat.getPageViews() : 0L,
                    stat.getVisitors() != null ? stat.getVisitors() : 0L,
                    stat.getAvgDurationMs(),
                    stat.getMeasured() != null ? stat.getMeasured() : 0L
            ));
        }
        return new VisitPageStatsResponseDto(from, to, visitLogRepository.averageDuration(start, end), pages);
    }

    private String toLabel(String path, Map<Long, String> blogTitles, Map<Long, String> projectTitles) {
        String fixed = PAGE_LABELS.get(path);
        if (fixed != null) {
            return fixed;
        }
        Matcher matcher = DETAIL_PATH.matcher(path);
        if (matcher.matches()) {
            Long id = Long.parseLong(matcher.group(2));
            String title = matcher.group(1).equals("blog") ? blogTitles.get(id) : projectTitles.get(id);
            String prefix = matcher.group(1).equals("blog") ? "블로그" : "포트폴리오";
            return title != null ? prefix + " · " + title : prefix + " · 삭제된 글";
        }
        return path;
    }

    private void validateRange(LocalDate from, LocalDate to) {
        if (to.isBefore(from)) {
            throw new BadRequestException("종료일은 시작일보다 빠를 수 없습니다.");
        }
        if (ChronoUnit.DAYS.between(from, to) >= MAX_STATS_DAYS) {
            throw new BadRequestException("통계 조회 기간은 최대 1년입니다.");
        }
    }

    @Override
    public VisitStatsResponseDto getStats(LocalDate from, LocalDate to) {
        validateRange(from, to);

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
