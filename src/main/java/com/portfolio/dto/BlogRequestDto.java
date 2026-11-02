package com.portfolio.dto;

import com.portfolio.domain.Blog;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 블로그 등록/수정 요청 DTO
 */
@Getter
@NoArgsConstructor
public class BlogRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 200, message = "제목은 200자 이하로 입력하세요.")
    private String title;

    /** 마크다운 본문 */
    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @Size(max = 500, message = "썸네일 URL은 500자 이하로 입력하세요.")
    private String thumbnailUrl;

    /** false 면 임시저장 (기본값 true = 공개) */
    private boolean published = true;

    /** 기술 스택 이름 목록 — 없는 이름은 자동 등록 */
    @Size(max = 30, message = "기술 스택은 30개 이하로 입력하세요.")
    private List<String> techStacks = new ArrayList<>();

    public Blog toEntity() {
        return Blog.builder()
                .title(title)
                .content(content)
                .thumbnailUrl(thumbnailUrl)
                .isPublished(published)
                .build();
    }
}
