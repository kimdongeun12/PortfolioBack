package com.portfolio.domain.blog.dto;

import com.portfolio.domain.blog.entity.Blog;
import com.portfolio.domain.techstack.entity.TechStack;

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
    private final LocalDateTime publishedAt;
    private final LocalDateTime createdAt;
    private final long commentCount;

    public BlogSummaryDto(Blog blog) {
        this(blog, 0L);
    }

    public BlogSummaryDto(Blog blog, long commentCount) {
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
        this.publishedAt = blog.getPublishedAt();
        this.createdAt = blog.getCreatedAt();
        this.commentCount = commentCount;
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
