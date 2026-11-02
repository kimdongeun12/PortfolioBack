package com.portfolio.controller;

import com.portfolio.domain.ProjectType;
import com.portfolio.dto.ProjectRequestDto;
import com.portfolio.dto.ProjectResponseDto;
import com.portfolio.service.ProjectService;
import com.portfolio.auth.AdminOnly;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * 프로젝트 REST API
 * [관리자 전용] 등록 · 수정 · 삭제
 *
 * GET  /api/projects               목록 조회 (?type=COMPANY|PERSONAL|SIDE 로 필터 가능)
 * GET  /api/projects/{id}          단건 조회
 * POST /api/projects               등록
 * PUT  /api/projects/{id}          수정
 * POST /api/projects/{id}/delete   삭제 (숨김 처리)
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> findAll(@RequestParam(required = false) ProjectType type) {
        return ResponseEntity.ok(projectService.findAll(type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    @AdminOnly
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody ProjectRequestDto requestDto) {
        Long id = projectService.create(requestDto);
        return ResponseEntity.created(URI.create("/api/projects/" + id)).body(id);
    }

    @AdminOnly
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> update(@PathVariable Long id,
                                                     @Valid @RequestBody ProjectRequestDto requestDto) {
        return ResponseEntity.ok(projectService.update(id, requestDto));
    }

    @AdminOnly
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
