package com.portfolio.domain.skill.controller;

import com.portfolio.auth.AdminOnly;
import com.portfolio.domain.skill.dto.SkillGroupRequestDto;
import com.portfolio.domain.skill.dto.SkillGroupResponseDto;
import com.portfolio.domain.skill.service.SkillGroupService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/skill-groups")
@RequiredArgsConstructor
public class SkillGroupController {

    private final SkillGroupService skillGroupService;

    @GetMapping
    public ResponseEntity<List<SkillGroupResponseDto>> findAll() {
        return ResponseEntity.ok(skillGroupService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillGroupResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(skillGroupService.findById(id));
    }

    @AdminOnly
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody SkillGroupRequestDto requestDto) {
        Long id = skillGroupService.create(requestDto);
        return ResponseEntity.created(URI.create("/api/skill-groups/" + id)).body(id);
    }

    @AdminOnly
    @PutMapping("/{id}")
    public ResponseEntity<SkillGroupResponseDto> update(@PathVariable Long id,
                                                        @Valid @RequestBody SkillGroupRequestDto requestDto) {
        return ResponseEntity.ok(skillGroupService.update(id, requestDto));
    }

    @AdminOnly
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        skillGroupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
