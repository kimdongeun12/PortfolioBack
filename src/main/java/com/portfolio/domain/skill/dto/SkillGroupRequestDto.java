package com.portfolio.domain.skill.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SkillGroupRequestDto {

    @NotBlank(message = "그룹명은 필수입니다.")
    @Size(max = 50, message = "그룹명은 50자 이하로 입력하세요.")
    private String name;

    @Size(max = 300, message = "설명은 300자 이하로 입력하세요.")
    private String description;

    private int sortOrder;

    private List<String> techStacks;
}
