package com.portfolio.domain.project.service;

import com.portfolio.domain.project.dto.ProjectRequestDto;
import com.portfolio.domain.project.dto.ProjectResponseDto;
import com.portfolio.domain.project.entity.ProjectType;

import java.util.List;

public interface ProjectService {

    List<ProjectResponseDto> findAll(ProjectType type);

    ProjectResponseDto findById(Long id);

    Long create(ProjectRequestDto requestDto);

    ProjectResponseDto update(Long id, ProjectRequestDto requestDto);

    void delete(Long id);
}
