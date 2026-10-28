package com.portfolio.dto;

import com.portfolio.domain.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 등록/수정 요청 DTO (클라이언트 → 서버)
 */
@Getter
@NoArgsConstructor
public class ProjectRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 100, message = "제목은 100자 이하로 입력하세요.")
    private String title;

    private String description;

    // TODO: 엔티티에 추가한 필드를 여기에도 추가하세요.

    public Project toEntity() {
        return Project.builder()
                .title(title)
                .description(description)
                .build();
    }
}
