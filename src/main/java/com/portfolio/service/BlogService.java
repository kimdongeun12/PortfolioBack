package com.portfolio.service;

import com.portfolio.dto.BlogRequestDto;
import com.portfolio.dto.BlogResponseDto;
import com.portfolio.dto.BlogSummaryDto;
import com.portfolio.dto.PageResponseDto;
import org.springframework.data.domain.Pageable;

/**
 * 블로그 비즈니스 로직 인터페이스
 */
public interface BlogService {

    /** 공개 글 목록 (tech 가 있으면 해당 기술 스택 글만) */
    PageResponseDto<BlogSummaryDto> findPublished(String tech, Pageable pageable);

    /** 공개 글 상세 (ipHash 로 좋아요 여부 표시) */
    BlogResponseDto findPublishedById(Long id, String ipHash);

    /** [관리자] 전체 목록 (임시저장 포함) */
    PageResponseDto<BlogSummaryDto> findAllForAdmin(Pageable pageable);

    /** [관리자] 상세 (임시저장 포함) */
    BlogResponseDto findByIdForAdmin(Long id);

    /** 등록 (생성된 id 반환) */
    Long create(BlogRequestDto requestDto);

    /** 수정 */
    BlogResponseDto update(Long id, BlogRequestDto requestDto);

    /** 삭제 (숨김 처리) */
    void delete(Long id);
}
