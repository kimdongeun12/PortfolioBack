package com.portfolio.domain.project.controller;

import com.portfolio.auth.AdminOnly;
import com.portfolio.domain.project.dto.ProjectOrderRequestDto;
import com.portfolio.domain.project.dto.ProjectRequestDto;
import com.portfolio.domain.project.dto.ProjectResponseDto;
import com.portfolio.domain.project.entity.ProjectType;
import com.portfolio.domain.project.service.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> findAll(@RequestParam(required = false) ProjectType type) {
        return ResponseEntity.ok(projectService.findAll(type));
    }

    @AdminOnly
    @PutMapping("/order")
    public ResponseEntity<Void> reorder(@Valid @RequestBody ProjectOrderRequestDto requestDto) {
        projectService.reorder(requestDto.getIds());
        return ResponseEntity.noContent().build();
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
