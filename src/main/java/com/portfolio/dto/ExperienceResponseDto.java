package com.portfolio.dto;

import com.portfolio.domain.Experience;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ExperienceResponseDto {

    private final Long id;
    private final String companyName;
    private final String formerName;
    private final String department;
    private final String jobTitle;
    private final String role;
    private final String position;
    private final String responsibilities;
    private final LocalDate startDate;
    private final LocalDate endDate;
    /** 재직중 여부 (endDate == null) */
    private final boolean current;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ExperienceResponseDto(Experience experience) {
        this.id = experience.getId();
        this.companyName = experience.getCompanyName();
        this.formerName = experience.getFormerName();
        this.department = experience.getDepartment();
        this.jobTitle = experience.getJobTitle();
        this.role = experience.getRole();
        this.position = experience.getPosition();
        this.responsibilities = experience.getResponsibilities();
        this.startDate = experience.getStartDate();
        this.endDate = experience.getEndDate();
        this.current = experience.isCurrent();
        this.createdAt = experience.getCreatedAt();
        this.updatedAt = experience.getUpdatedAt();
    }
}
