package com.portfolio.service;

import com.portfolio.dto.ExperienceRequestDto;
import com.portfolio.dto.ExperienceResponseDto;

import java.util.List;

/**
 * 경력 비즈니스 로직 인터페이스
 */
public interface ExperienceService {

    /** 전체 목록 (최신 입사일 순) */
    List<ExperienceResponseDto> findAll();

    /** 단건 조회 */
    ExperienceResponseDto findById(Long id);

    /** 등록 (생성된 id 반환) */
    Long create(ExperienceRequestDto requestDto);

    /** 수정 */
    ExperienceResponseDto update(Long id, ExperienceRequestDto requestDto);

    /** 삭제 (숨김 처리) */
    void delete(Long id);
}
