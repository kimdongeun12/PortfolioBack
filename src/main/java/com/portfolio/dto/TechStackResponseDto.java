package com.portfolio.dto;

import com.portfolio.domain.TechStack;
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
