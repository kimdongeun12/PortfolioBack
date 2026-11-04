package com.portfolio.dto;

import com.portfolio.domain.Blog;
import com.portfolio.domain.TechStack;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BlogResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final String thumbnailUrl;
    private final int likeCount;
    private final boolean liked;
    private final boolean published;
    private final List<String> techStacks;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public BlogResponseDto(Blog blog, boolean liked) {
        this.id = blog.getId();
        this.title = blog.getTitle();
        this.content = blog.getContent();
        this.thumbnailUrl = blog.getThumbnailUrl();
        this.likeCount = blog.getLikeCount();
        this.liked = liked;
        this.published = blog.isPublished();
        this.techStacks = blog.getTechStacks().stream()
                .map(TechStack::getName)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
        this.createdAt = blog.getCreatedAt();
        this.updatedAt = blog.getUpdatedAt();
    }
}
