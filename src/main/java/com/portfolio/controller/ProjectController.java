package com.portfolio.controller;

import com.portfolio.dto.ProjectRequestDto;
import com.portfolio.dto.ProjectResponseDto;
import com.portfolio.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * 프로젝트 REST API
 *
 * GET    /api/projects        목록 조회
 * GET    /api/projects/{id}   단건 조회
 * POST   /api/projects        등록
 * PUT    /api/projects/{id}   수정
 * DELETE /api/projects/{id}   삭제
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> findAll() {
        return ResponseEntity.ok(projectService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody ProjectRequestDto requestDto) {
        Long id = projectService.create(requestDto);
        return ResponseEntity.created(URI.create("/api/projects/" + id)).body(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> update(@PathVariable Long id,
                                                     @Valid @RequestBody ProjectRequestDto requestDto) {
        return ResponseEntity.ok(projectService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
