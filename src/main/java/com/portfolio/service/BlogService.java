package com.portfolio.service;

import com.portfolio.dto.BlogRequestDto;
import com.portfolio.dto.BlogResponseDto;
import com.portfolio.dto.BlogSummaryDto;
import com.portfolio.dto.PageResponseDto;
import org.springframework.data.domain.Pageable;

public interface BlogService {

    PageResponseDto<BlogSummaryDto> findPublished(String tech, Pageable pageable);

    BlogResponseDto findPublishedById(Long id, String ipHash);

    PageResponseDto<BlogSummaryDto> findAllForAdmin(Pageable pageable);

    BlogResponseDto findByIdForAdmin(Long id);

    Long create(BlogRequestDto requestDto);

    BlogResponseDto update(Long id, BlogRequestDto requestDto);

    void delete(Long id);
}
