package com.portfolio.domain.project.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectOrderRequestDto {

    @NotEmpty(message = "정렬할 프로젝트 목록이 비어 있습니다.")
    private List<Long> ids;
}
