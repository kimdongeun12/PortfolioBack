package com.portfolio.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * 페이징 응답 공통 DTO
 * (Spring Data 의 Page 를 그대로 반환하면 JSON 구조가 버전마다 달라질 수 있어 고정된 형태로 변환합니다)
 */
@Getter
public class PageResponseDto<T> {

    private final List<T> content;
    private final int page;          // 0부터 시작
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean last;

    public <E> PageResponseDto(Page<E> page, Function<E, T> mapper) {
        this.content = page.getContent().stream().map(mapper).toList();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.last = page.isLast();
    }
}
