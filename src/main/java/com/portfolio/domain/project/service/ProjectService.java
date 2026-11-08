package com.portfolio.domain.project.service;

import com.portfolio.global.common.PageResponseDto;
import com.portfolio.domain.project.dto.ProjectRequestDto;
import com.portfolio.domain.project.dto.ProjectResponseDto;
import com.portfolio.domain.project.entity.ProjectType;

import java.util.List;

public interface ProjectService {

    List<ProjectResponseDto> findAll(ProjectType type);

    PageResponseDto<ProjectResponseDto> findPage(int page, int size);

    ProjectResponseDto findById(Long id);

    Long create(ProjectRequestDto requestDto);

    ProjectResponseDto update(Long id, ProjectRequestDto requestDto);

    void delete(Long id);

    void reorder(List<Long> ids);
}
