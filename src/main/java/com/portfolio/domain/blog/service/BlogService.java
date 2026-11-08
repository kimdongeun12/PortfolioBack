package com.portfolio.domain.blog.service;

import com.portfolio.domain.blog.dto.BlogOverviewDto;
import com.portfolio.domain.blog.dto.BlogRequestDto;
import com.portfolio.domain.blog.dto.BlogResponseDto;
import com.portfolio.domain.blog.dto.BlogSummaryDto;
import com.portfolio.global.common.PageResponseDto;

import org.springframework.data.domain.Pageable;

public interface BlogService {

    PageResponseDto<BlogSummaryDto> findPublished(String tech, String q, Pageable pageable);

    BlogOverviewDto getOverview();

    BlogResponseDto findPublishedById(Long id, String ipHash);

    PageResponseDto<BlogSummaryDto> findAllForAdmin(Pageable pageable);

    BlogResponseDto findByIdForAdmin(Long id);

    Long create(BlogRequestDto requestDto);

    BlogResponseDto update(Long id, BlogRequestDto requestDto);

    void delete(Long id);
}
