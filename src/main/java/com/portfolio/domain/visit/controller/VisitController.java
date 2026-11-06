package com.portfolio.domain.visit.controller;

import com.portfolio.auth.AdminOnly;
import com.portfolio.domain.visit.dto.VisitRequestDto;
import com.portfolio.domain.visit.dto.VisitStatsResponseDto;
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

    @PostMapping
    public ResponseEntity<Void> record(@Valid @RequestBody VisitRequestDto requestDto, HttpServletRequest request) {
        visitService.record(requestDto, clientInfoResolver.getIpHash(request), clientInfoResolver.getUserAgent(request));
        return ResponseEntity.noContent().build();
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
}
