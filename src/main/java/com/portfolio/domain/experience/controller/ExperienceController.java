package com.portfolio.domain.experience.controller;

import com.portfolio.auth.AdminOnly;
import com.portfolio.domain.experience.dto.ExperienceRequestDto;
import com.portfolio.domain.experience.dto.ExperienceResponseDto;
import com.portfolio.domain.experience.service.ExperienceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
