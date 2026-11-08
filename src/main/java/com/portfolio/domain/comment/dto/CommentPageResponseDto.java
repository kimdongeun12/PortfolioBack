package com.portfolio.domain.comment.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CommentPageResponseDto {

    private final List<CommentResponseDto> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean last;
    private final long totalCount;
    private final long todayCount;

    public CommentPageResponseDto(List<CommentResponseDto> content, int page, int size, long totalElements,
                                  int totalPages, boolean last, long totalCount, long todayCount) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
        this.totalCount = totalCount;
        this.todayCount = todayCount;
    }
}
