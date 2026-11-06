package com.portfolio.domain.project.dto;

import com.portfolio.domain.experience.entity.Experience;
import com.portfolio.domain.project.entity.Project;
import com.portfolio.domain.project.entity.ProjectType;
import com.portfolio.domain.techstack.entity.TechStack;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProjectResponseDto {

    private final Long id;
    private final Long experienceId;
    private final String companyName;
    private final String formerName;
    private final String department;
    private final String position;
    private final String title;
    private final String summary;
    private final String role;
    private final String responsibilities;
    private final String content;
    private final ProjectType type;
    private final String thumbnailUrl;
    private final boolean pinned;
    private final int sortOrder;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<String> techStacks;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ProjectResponseDto(Project project) {
        Experience experience = project.getExperience();
        this.id = project.getId();
        this.experienceId = experience != null ? experience.getId() : null;
        this.companyName = experience != null ? experience.getCompanyName() : null;
        this.formerName = experience != null ? experience.getFormerName() : null;
        this.department = experience != null ? experience.getDepartment() : null;
        this.position = experience != null ? experience.getPosition() : null;
        this.title = project.getTitle();
        this.summary = project.getSummary();
        this.role = project.getRole();
        this.responsibilities = project.getResponsibilities();
        this.content = project.getContent();
        this.type = project.getType();
        this.thumbnailUrl = project.getThumbnailUrl();
        this.pinned = project.isPinned();
        this.sortOrder = project.getSortOrder();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
        this.techStacks = project.getTechStacks().stream()
                .map(TechStack::getName)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
        this.createdAt = project.getCreatedAt();
        this.updatedAt = project.getUpdatedAt();
    }
}
