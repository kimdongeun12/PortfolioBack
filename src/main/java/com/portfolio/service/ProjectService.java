package com.portfolio.service;

import com.portfolio.domain.ProjectType;
import com.portfolio.dto.ProjectRequestDto;
import com.portfolio.dto.ProjectResponseDto;

import java.util.List;

public interface ProjectService {

    List<ProjectResponseDto> findAll(ProjectType type);

    ProjectResponseDto findById(Long id);

    Long create(ProjectRequestDto requestDto);

    ProjectResponseDto update(Long id, ProjectRequestDto requestDto);

    void delete(Long id);
}
