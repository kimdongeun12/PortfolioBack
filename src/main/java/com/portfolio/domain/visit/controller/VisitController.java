package com.portfolio.domain.visit.controller;

import com.portfolio.auth.AdminAuthService;
import com.portfolio.auth.AdminCookieManager;
import com.portfolio.auth.AdminOnly;
import com.portfolio.domain.visit.dto.VisitPageStatsResponseDto;
import com.portfolio.domain.visit.dto.VisitRecordResponseDto;
import com.portfolio.domain.visit.dto.VisitRequestDto;
import com.portfolio.domain.visit.dto.VisitStatsResponseDto;
import com.portfolio.domain.visit.dto.VisitTotalResponseDto;
import com.portfolio.domain.visit.dto.VisitTrendResponseDto;
import com.portfolio.domain.visit.dto.VisitorLabelRequestDto;
import com.portfolio.domain.visit.dto.VisitorTimelineResponseDto;
import com.portfolio.domain.visit.service.VisitService;
import com.portfolio.global.util.ClientInfoResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final ClientInfoResolver clientInfoResolver;
    private final AdminCookieManager adminCookieManager;
    private final AdminAuthService adminAuthService;

    @PostMapping
    public ResponseEntity<VisitRecordResponseDto> record(@Valid @RequestBody VisitRequestDto requestDto, HttpServletRequest request) {
        String token = adminCookieManager.resolveToken(request);
        if (token != null && adminAuthService.isAuthenticated(token)) {
            return ResponseEntity.ok(new VisitRecordResponseDto(null));
        }
        Long id = visitService.record(requestDto, clientInfoResolver.getIpHash(request), clientInfoResolver.getUserAgent(request));
        return ResponseEntity.ok(new VisitRecordResponseDto(id));
    }

    @PostMapping("/duration")
    public ResponseEntity<Void> duration(@RequestParam Long id,
                                         @RequestParam String sessionId,
                                         @RequestParam int durationMs) {
        visitService.updateDuration(id, sessionId, durationMs);
        return ResponseEntity.noContent().build();
    }

    @AdminOnly
    @GetMapping("/pages")
    public ResponseEntity<VisitPageStatsResponseDto> pages(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        LocalDate end = (to != null) ? to : LocalDate.now();
        LocalDate start = (from != null) ? from : end.minusDays(29);
        return ResponseEntity.ok(visitService.getPageStats(start, end));
    }

    @GetMapping("/total")
    public ResponseEntity<VisitTotalResponseDto> total() {
        return ResponseEntity.ok(visitService.getTotal());
    }

    @AdminOnly
    @GetMapping("/stats")
    public ResponseEntity<VisitStatsResponseDto> stats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        LocalDate end = (to != null) ? to : LocalDate.now();
        LocalDate start = (from != null) ? from : end.minusDays(29);
        return ResponseEntity.ok(visitService.getStats(start, end));
    }

    @AdminOnly
    @GetMapping("/trend")
    public ResponseEntity<VisitTrendResponseDto> trend(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "day") String unit,
            @RequestParam(required = false) String path) {
        LocalDate end = (to != null) ? to : LocalDate.now();
        LocalDate start = (from != null) ? from : end.minusDays(29);
        return ResponseEntity.ok(visitService.getTrend(start, end, unit, path));
    }

    @AdminOnly
    @GetMapping("/visitors")
    public ResponseEntity<VisitorTimelineResponseDto> visitors(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        LocalDate end = (to != null) ? to : LocalDate.now();
        LocalDate start = (from != null) ? from : end.minusDays(6);
        return ResponseEntity.ok(visitService.getVisitors(start, end));
    }

    @AdminOnly
    @PutMapping("/visitors/{ipHash}/label")
    public ResponseEntity<Void> label(@PathVariable String ipHash, @Valid @RequestBody VisitorLabelRequestDto requestDto) {
        visitService.updateVisitorLabel(ipHash, requestDto.getLabel());
        return ResponseEntity.noContent().build();
    }
}
