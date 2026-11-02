package com.portfolio.service;

import com.portfolio.domain.ProjectType;
import com.portfolio.dto.ProjectRequestDto;
import com.portfolio.dto.ProjectResponseDto;

import java.util.List;

/**
 * 프로젝트 비즈니스 로직 인터페이스
 * 구현은 service/impl/ProjectServiceImpl 에서 합니다.
 */
public interface ProjectService {

    /** 목록 조회 (type 이 null 이면 전체) */
    List<ProjectResponseDto> findAll(ProjectType type);

    /** 단건 조회 */
    ProjectResponseDto findById(Long id);

    /** 등록 (생성된 id 반환) */
    Long create(ProjectRequestDto requestDto);

    /** 수정 */
    ProjectResponseDto update(Long id, ProjectRequestDto requestDto);

    /** 삭제 (숨김 처리) */
    void delete(Long id);
}
