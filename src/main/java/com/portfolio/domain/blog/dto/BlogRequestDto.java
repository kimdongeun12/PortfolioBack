package com.portfolio.domain.blog.dto;

import com.portfolio.domain.blog.entity.Blog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class BlogRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 200, message = "제목은 200자 이하로 입력하세요.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    @Size(max = 500, message = "썸네일 URL은 500자 이하로 입력하세요.")
    private String thumbnailUrl;

    private boolean published = true;

    private LocalDateTime publishedAt;

    @Size(max = 30, message = "기술 스택은 30개 이하로 입력하세요.")
    private List<String> techStacks = new ArrayList<>();

    public Blog toEntity() {
        return Blog.builder()
                .title(title)
                .content(content)
                .thumbnailUrl(thumbnailUrl)
                .isPublished(published)
                .publishedAt(publishedAt)
                .build();
    }
}
