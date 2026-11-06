package com.portfolio.domain.techstack.dto;

import com.portfolio.domain.techstack.entity.TechStack;

import lombok.Getter;

@Getter
public class TechStackResponseDto {

    private final Long id;
    private final String name;

    public TechStackResponseDto(TechStack techStack) {
        this.id = techStack.getId();
        this.name = techStack.getName();
    }
}
