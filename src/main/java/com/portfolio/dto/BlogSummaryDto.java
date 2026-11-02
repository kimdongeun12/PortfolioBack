package com.portfolio.dto;

import com.portfolio.domain.Blog;
import com.portfolio.domain.TechStack;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 블로그 목록용 DTO (본문 대신 앞부분 미리보기만 내려줍니다)
 */
@Getter
public class BlogSummaryDto {

    private static final int EXCERPT_LENGTH = 150;

    private final Long id;
    private final String title;
    private final String excerpt;
    private final String thumbnailUrl;
    private final int likeCount;
    /** 공개 여부 (공개 목록에서는 항상 true, 관리자 목록에서 임시저장 구분용) */
    private final boolean published;
    private final List<String> techStacks;
    private final LocalDateTime createdAt;

    public BlogSummaryDto(Blog blog) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.excerpt = toExcerpt(blog.getContent());
        this.thumbnailUrl = blog.getThumbnailUrl();
        this.likeCount = blog.getLikeCount();
        this.published = blog.isPublished();
        this.techStacks = blog.getTechStacks().stream()
                .map(TechStack::getName)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
        this.createdAt = blog.getCreatedAt();
    }

    /** 마크다운 기호·코드블록·이미지를 대략 걷어내고 앞부분만 자릅니다. */
    private static String toExcerpt(String markdown) {
        if (markdown == null) {
            return "";
        }
        String text = markdown
                .replaceAll("(?s)```.*?```", " ")          // 코드 블록
                .replaceAll("!\\[[^\\]]*\\]\\([^)]*\\)", " ")  // 이미지
                .replaceAll("\\[([^\\]]*)\\]\\([^)]*\\)", "$1") // 링크는 텍스트만
                .replaceAll("[#>*`_~|-]", " ")
                .replaceAll("\\s+", " ")
                .trim();
        return text.length() > EXCERPT_LENGTH ? text.substring(0, EXCERPT_LENGTH) + "…" : text;
    }
}
