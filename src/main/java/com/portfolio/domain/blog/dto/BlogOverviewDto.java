package com.portfolio.domain.blog.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class BlogOverviewDto {

    private final long posts;
    private final long likes;
    private final long comments;
    private final List<String> tags;

    public BlogOverviewDto(long posts, long likes, long comments, List<String> tags) {
        this.posts = posts;
        this.likes = likes;
        this.comments = comments;
        this.tags = tags;
    }
}
