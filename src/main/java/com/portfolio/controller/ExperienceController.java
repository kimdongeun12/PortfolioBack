package com.portfolio.controller;

import com.portfolio.dto.ExperienceRequestDto;
import com.portfolio.dto.ExperienceResponseDto;
import com.portfolio.service.ExperienceService;
import com.portfolio.auth.AdminOnly;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * 경력 REST API
 * [관리자 전용] 등록 · 수정 · 삭제
 *
 * GET  /api/experiences              목록 조회 (최신 입사일 순)
 * GET  /api/experiences/{id}         단건 조회
 * POST /api/experiences              등록
 * PUT  /api/experiences/{id}         수정
 * POST /api/experiences/{id}/delete  삭제 (숨김 처리)
 */
@RestController
@RequestMapping("/api/experiences")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    @GetMapping
    public ResponseEntity<List<ExperienceResponseDto>> findAll() {
        return ResponseEntity.ok(experienceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExperienceResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(experienceService.findById(id));
    }

    @AdminOnly
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody ExperienceRequestDto requestDto) {
        Long id = experienceService.create(requestDto);
        return ResponseEntity.created(URI.create("/api/experiences/" + id)).body(id);
    }

    @AdminOnly
    @PutMapping("/{id}")
    public ResponseEntity<ExperienceResponseDto> update(@PathVariable Long id,
                                                        @Valid @RequestBody ExperienceRequestDto requestDto) {
        return ResponseEntity.ok(experienceService.update(id, requestDto));
    }

    @AdminOnly
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        experienceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
