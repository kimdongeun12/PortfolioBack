package com.portfolio.domain.experience.dto;

import com.portfolio.domain.experience.entity.Experience;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ExperienceRequestDto {

    @NotBlank(message = "회사명은 필수입니다.")
    @Size(max = 100, message = "회사명은 100자 이하로 입력하세요.")
    private String companyName;

    @Size(max = 100, message = "이전 사명은 100자 이하로 입력하세요.")
    private String formerName;

    @Size(max = 100, message = "부서는 100자 이하로 입력하세요.")
    private String department;

    @Size(max = 100, message = "담당 직무는 100자 이하로 입력하세요.")
    private String jobTitle;

    @Size(max = 200, message = "역할은 200자 이하로 입력하세요.")
    private String role;

    @Size(max = 50, message = "직급/직책은 50자 이하로 입력하세요.")
    private String position;

    private String responsibilities;

    private String achievements;

    @NotNull(message = "입사일은 필수입니다.")
    private LocalDate startDate;

    private LocalDate endDate;

    public Experience toEntity() {
        return Experience.builder()
                .companyName(companyName)
                .formerName(formerName)
                .department(department)
                .jobTitle(jobTitle)
                .role(role)
                .position(position)
                .responsibilities(responsibilities)
                .achievements(achievements)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
