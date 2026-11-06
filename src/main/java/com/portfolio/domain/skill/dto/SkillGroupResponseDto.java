package com.portfolio.domain.skill.dto;

import com.portfolio.domain.skill.entity.SkillGroup;
import com.portfolio.domain.techstack.entity.TechStack;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
public class SkillGroupResponseDto {

    private final Long id;
    private final String name;
    private final String description;
    private final int sortOrder;
    private final List<String> techStacks;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public SkillGroupResponseDto(SkillGroup skillGroup) {
        this.id = skillGroup.getId();
        this.name = skillGroup.getName();
        this.description = skillGroup.getDescription();
        this.sortOrder = skillGroup.getSortOrder();
        this.techStacks = skillGroup.getTechStacks().stream()
                .filter(Objects::nonNull)
                .map(TechStack::getName)
                .toList();
        this.createdAt = skillGroup.getCreatedAt();
        this.updatedAt = skillGroup.getUpdatedAt();
    }
}
