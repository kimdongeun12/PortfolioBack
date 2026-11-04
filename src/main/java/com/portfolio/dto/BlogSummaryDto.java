package com.portfolio.dto;

import com.portfolio.domain.Blog;
import com.portfolio.domain.TechStack;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BlogSummaryDto {

    private static final int EXCERPT_LENGTH = 150;

    private final Long id;
    private final String title;
    private final String excerpt;
    private final String thumbnailUrl;
    private final int likeCount;
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

    private static String toExcerpt(String markdown) {
        if (markdown == null) {
            return "";
        }
        String text = markdown
                .replaceAll("(?s)```.*?```", " ")
                .replaceAll("!\\[[^\\]]*\\]\\([^)]*\\)", " ")
                .replaceAll("\\[([^\\]]*)\\]\\([^)]*\\)", "$1")
                .replaceAll("[#>*`_~|-]", " ")
                .replaceAll("\\s+", " ")
                .trim();
        return text.length() > EXCERPT_LENGTH ? text.substring(0, EXCERPT_LENGTH) + "…" : text;
    }
}
