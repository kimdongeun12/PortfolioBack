package com.portfolio.domain.project.dto;

import com.portfolio.domain.experience.entity.Experience;
import com.portfolio.domain.project.entity.Project;
import com.portfolio.domain.project.entity.ProjectType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectRequestDto {

    private Long experienceId;

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 200, message = "제목은 200자 이하로 입력하세요.")
    private String title;

    @Size(max = 300, message = "한 줄 소개는 300자 이하로 입력하세요.")
    private String summary;

    @Size(max = 200, message = "담당 역할은 200자 이하로 입력하세요.")
    private String role;

    private String responsibilities;

    private String content;

    @NotNull(message = "프로젝트 종류는 필수입니다. (COMPANY / PERSONAL / SIDE)")
    private ProjectType type;

    @Size(max = 500, message = "썸네일 URL은 500자 이하로 입력하세요.")
    private String thumbnailUrl;

    @Size(max = 40, message = "커버 라벨은 40자 이하로 입력하세요.")
    private String coverLabel;

    @Size(max = 60, message = "커버 제목은 60자 이하로 입력하세요.")
    private String coverTitle;

    @Size(max = 100, message = "커버 보조 문구는 100자 이하로 입력하세요.")
    private String coverSub;

    @Size(max = 40, message = "기간 표기는 40자 이하로 입력하세요.")
    private String periodLabel;

    private boolean pinned;

    private int sortOrder;

    private LocalDate startDate;

    private LocalDate endDate;

    @Size(max = 30, message = "기술 스택은 30개 이하로 입력하세요.")
    private List<String> techStacks = new ArrayList<>();

    public Project toEntity(Experience experience) {
        return Project.builder()
                .experience(experience)
                .title(title)
                .summary(summary)
                .role(role)
                .responsibilities(responsibilities)
                .content(content)
                .type(type)
                .thumbnailUrl(thumbnailUrl)
                .isPinned(pinned)
                .sortOrder(sortOrder)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
