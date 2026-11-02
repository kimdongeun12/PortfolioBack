package com.portfolio.dto;

import com.portfolio.domain.Experience;
import com.portfolio.domain.Project;
import com.portfolio.domain.ProjectType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 프로젝트 등록/수정 요청 DTO
 */
@Getter
@NoArgsConstructor
public class ProjectRequestDto {

    /** 소속 경력 id (개인/사이드 프로젝트는 null) */
    private Long experienceId;

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 200, message = "제목은 200자 이하로 입력하세요.")
    private String title;

    @Size(max = 300, message = "한 줄 소개는 300자 이하로 입력하세요.")
    private String summary;

    @Size(max = 200, message = "담당 역할은 200자 이하로 입력하세요.")
    private String role;

    private String responsibilities;

    /** 마크다운 본문 */
    private String content;

    @NotNull(message = "프로젝트 종류는 필수입니다. (COMPANY / PERSONAL / SIDE)")
    private ProjectType type;

    @Size(max = 500, message = "썸네일 URL은 500자 이하로 입력하세요.")
    private String thumbnailUrl;

    private boolean pinned;

    private int sortOrder;

    private LocalDate startDate;

    /** null 이면 진행중 */
    private LocalDate endDate;

    /** 기술 스택 이름 목록 (예: ["Vue", "TypeScript"]) — 없는 이름은 자동 등록 */
    @Size(max = 30, message = "기술 스택은 30개 이하로 입력하세요.")
    private List<String> techStacks = new ArrayList<>();

    /** experience 는 Service 에서 experienceId 로 조회해서 넘겨줍니다. */
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
