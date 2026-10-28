package com.portfolio.dto;

import com.portfolio.domain.Project;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 응답 DTO (서버 → 클라이언트)
 * 엔티티를 그대로 반환하지 않고 DTO로 변환해서 내려줍니다.
 */
@Getter
public class ProjectResponseDto {

    private final Long id;
    private final String title;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ProjectResponseDto(Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.createdAt = project.getCreatedAt();
        this.updatedAt = project.getUpdatedAt();
    }
}
