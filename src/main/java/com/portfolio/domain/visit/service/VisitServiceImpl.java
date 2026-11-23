package com.portfolio.domain.visit.service;

import com.portfolio.domain.blog.entity.Blog;
import com.portfolio.domain.blog.repository.BlogRepository;
import com.portfolio.domain.project.entity.Project;
import com.portfolio.domain.project.repository.ProjectRepository;
import com.portfolio.domain.visit.dto.VisitPageStatsResponseDto;
import com.portfolio.domain.visit.dto.VisitRequestDto;
import com.portfolio.domain.visit.dto.VisitStatsResponseDto;
import com.portfolio.domain.visit.dto.VisitTrendResponseDto;
import com.portfolio.domain.visit.dto.VisitorTimelineResponseDto;
import com.portfolio.domain.visit.entity.VisitLog;
import com.portfolio.domain.visit.entity.VisitorLabel;
import com.portfolio.domain.visit.repository.DailyVisitStat;
import com.portfolio.domain.visit.repository.PageVisitStat;
import com.portfolio.domain.visit.repository.PeriodVisitStat;
import com.portfolio.domain.visit.repository.VisitLogRepository;
import com.portfolio.domain.visit.repository.VisitorLabelRepository;
import com.portfolio.global.exception.BadRequestException;
import com.portfolio.global.util.ClientInfoResolver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
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
    private static final long MAX_HOURLY_DAYS = 7;
    private static final long MAX_VISITOR_DAYS = 31;
    private static final int VISITOR_LOG_LIMIT = 3000;
    private static final int SUSPECT_MIN_PAGE_VIEWS = 3;
    private static final DateTimeFormatter HOUR_KEY = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00");
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
    private final VisitorLabelRepository visitorLabelRepository;

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
                .device(UserAgentSummary.device(userAgent))
                .os(UserAgentSummary.os(userAgent))
                .browser(UserAgentSummary.browser(userAgent))
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
                .filter(log -> log.getDurationMs() == null || log.getDurationMs() < Math.min(durationMs, MAX_DURATION_MS))
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

    @Override
    public VisitTrendResponseDto getTrend(LocalDate from, LocalDate to, String unit, String path) {
        validateRange(from, to);
        String resolvedUnit = switch (unit == null ? "" : unit) {
            case "month" -> "month";
            case "hour" -> "hour";
            default -> "day";
        };
        if ("hour".equals(resolvedUnit) && ChronoUnit.DAYS.between(from, to) >= MAX_HOURLY_DAYS) {
            throw new BadRequestException("시간별 통계는 최대 7일까지 조회할 수 있습니다.");
        }
        String target = blankToNull(path == null ? null : path.trim());
        String format = switch (resolvedUnit) {
            case "month" -> "%Y-%m";
            case "hour" -> "%Y-%m-%d %H:00";
            default -> "%Y-%m-%d";
        };

        Map<String, PeriodVisitStat> statByPeriod = new HashMap<>();
        for (PeriodVisitStat stat : visitLogRepository.findPeriodStats(
                format, from.atStartOfDay(), to.plusDays(1).atStartOfDay(), target)) {
            statByPeriod.put(stat.getPeriod(), stat);
        }

        List<String> keys = new ArrayList<>();
        if ("month".equals(resolvedUnit)) {
            for (YearMonth month = YearMonth.from(from); !month.isAfter(YearMonth.from(to)); month = month.plusMonths(1)) {
                keys.add(month.toString());
            }
        } else if ("hour".equals(resolvedUnit)) {
            LocalDateTime last = to.plusDays(1).atStartOfDay();
            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(1);
            if (now.isBefore(last)) {
                last = now;
            }
            for (LocalDateTime hour = from.atStartOfDay(); hour.isBefore(last); hour = hour.plusHours(1)) {
                keys.add(hour.format(HOUR_KEY));
            }
        } else {
            for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
                keys.add(date.toString());
            }
        }

        List<VisitTrendResponseDto.Period> periods = new ArrayList<>();
        for (String key : keys) {
            PeriodVisitStat stat = statByPeriod.get(key);
            periods.add(new VisitTrendResponseDto.Period(
                    key,
                    stat != null && stat.getPageViews() != null ? stat.getPageViews() : 0L,
                    stat != null && stat.getVisitors() != null ? stat.getVisitors() : 0L,
                    stat != null ? stat.getAvgDurationMs() : null,
                    stat != null && stat.getMeasured() != null ? stat.getMeasured() : 0L
            ));
        }
        return new VisitTrendResponseDto(from, to, resolvedUnit, target, periods);
    }

    @Override
    public VisitorTimelineResponseDto getVisitors(LocalDate from, LocalDate to) {
        validateRange(from, to);
        if (ChronoUnit.DAYS.between(from, to) >= MAX_VISITOR_DAYS) {
            throw new BadRequestException("방문자 타임라인은 최대 31일까지 조회할 수 있습니다.");
        }
        List<VisitLog> logs = visitLogRepository.findTop3000ByVisitedAtGreaterThanEqualAndVisitedAtLessThanOrderByVisitedAtDesc(
                from.atStartOfDay(), to.plusDays(1).atStartOfDay());
        Map<String, String> pageLabels = resolveLabels(logs.stream().map(VisitLog::getPath).toList());

        Map<String, List<VisitLog>> byVisitor = new LinkedHashMap<>();
        for (VisitLog log : logs) {
            String key = log.getIpHash() != null ? log.getIpHash() : "s:" + log.getSessionId();
            byVisitor.computeIfAbsent(key, k -> new ArrayList<>()).add(log);
        }
        Map<String, String> labels = new HashMap<>();
        for (VisitorLabel label : visitorLabelRepository.findAllById(
                byVisitor.keySet().stream().filter(k -> !k.startsWith("s:")).toList())) {
            labels.put(label.getIpHash(), label.getLabel());
        }

        List<VisitorTimelineResponseDto.Visitor> visitors = new ArrayList<>();
        for (Map.Entry<String, List<VisitLog>> entry : byVisitor.entrySet()) {
            List<VisitLog> visits = new ArrayList<>(entry.getValue());
            visits.sort(Comparator.comparing(VisitLog::getVisitedAt));
            VisitLog first = visits.get(0);
            VisitLog last = visits.get(visits.size() - 1);
            String ipHash = entry.getKey().startsWith("s:") ? null : entry.getKey();
            long totalDuration = visits.stream().mapToLong(v -> v.getDurationMs() != null ? v.getDurationMs() : 0).sum();
            long sessions = visits.stream().map(VisitLog::getSessionId).distinct().count();
            String firstReferrer = visits.stream().map(VisitLog::getReferrer)
                    .filter(r -> r != null && !r.isBlank()).findFirst().map(this::toHost).orElse(null);

            List<VisitorTimelineResponseDto.Visit> items = visits.stream().map(v -> new VisitorTimelineResponseDto.Visit(
                    v.getVisitedAt(), v.getPath(), pageLabels.getOrDefault(v.getPath(), v.getPath()),
                    v.getDurationMs(), toHost(v.getReferrer()), v.getSessionId())).toList();

            visitors.add(new VisitorTimelineResponseDto.Visitor(
                    entry.getKey().startsWith("s:") ? entry.getKey() : entry.getKey().substring(0, 8),
                    ipHash,
                    ipHash != null ? labels.get(ipHash) : null,
                    orUnknown(last.getDevice()),
                    orUnknown(last.getBrowser()),
                    orUnknown(last.getOs()),
                    visits.stream().anyMatch(VisitLog::isBotLike)
                            || (visits.size() >= SUSPECT_MIN_PAGE_VIEWS && sessions == visits.size() && totalDuration == 0),
                    firstReferrer,
                    first.getVisitedAt(),
                    last.getVisitedAt(),
                    visits.size(),
                    sessions,
                    totalDuration,
                    items
            ));
        }
        visitors.sort(Comparator.comparing(VisitorTimelineResponseDto.Visitor::getLastVisitedAt).reversed());
        return new VisitorTimelineResponseDto(from, to, logs.size() >= VISITOR_LOG_LIMIT, visitors);
    }

    @Override
    @Transactional
    public void updateVisitorLabel(String ipHash, String label) {
        if (ipHash == null || !ipHash.matches("^[0-9a-f]{64}$")) {
            throw new BadRequestException("방문자 정보가 올바르지 않습니다.");
        }
        String value = blankToNull(label == null ? null : label.trim());
        if (value == null) {
            visitorLabelRepository.deleteById(ipHash);
            return;
        }
        visitorLabelRepository.findById(ipHash).ifPresentOrElse(
                saved -> saved.changeLabel(value),
                () -> visitorLabelRepository.save(new VisitorLabel(ipHash, value)));
    }

    private Map<String, String> resolveLabels(List<String> paths) {
        Set<Long> blogIds = new HashSet<>();
        Set<Long> projectIds = new HashSet<>();
        for (String path : paths) {
            Matcher matcher = DETAIL_PATH.matcher(path);
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
        Map<String, String> labels = new HashMap<>();
        for (String path : new HashSet<>(paths)) {
            labels.put(path, toLabel(path, blogTitles, projectTitles));
        }
        return labels;
    }

    private String orUnknown(String value) {
        return value != null ? value : "알 수 없음";
    }

    private String toHost(String referrer) {
        if (referrer == null || referrer.isBlank()) {
            return null;
        }
        try {
            String host = URI.create(referrer.trim()).getHost();
            return host != null ? host.replaceFirst("^www\\.", "") : referrer;
        } catch (IllegalArgumentException e) {
            return referrer;
        }
    }

    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }
}
