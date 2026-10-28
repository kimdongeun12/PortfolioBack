package com.portfolio.service.impl;

import com.portfolio.domain.Project;
import com.portfolio.dto.ProjectRequestDto;
import com.portfolio.dto.ProjectResponseDto;
import com.portfolio.exception.NotFoundException;
import com.portfolio.repository.ProjectRepository;
import com.portfolio.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public List<ProjectResponseDto> findAll() {
        return projectRepository.findAll().stream()
                .map(ProjectResponseDto::new)
                .toList();
    }

    @Override
    public ProjectResponseDto findById(Long id) {
        return new ProjectResponseDto(getProject(id));
    }

    @Override
    @Transactional
    public Long create(ProjectRequestDto requestDto) {
        // TODO: 등록 전 검증 로직이 필요하면 여기에 추가하세요.
        return projectRepository.save(requestDto.toEntity()).getId();
    }

    @Override
    @Transactional
    public ProjectResponseDto update(Long id, ProjectRequestDto requestDto) {
        Project project = getProject(id);
        // save() 호출 없이 변경 감지(dirty checking)로 UPDATE 됩니다.
        project.update(requestDto.getTitle(), requestDto.getDescription());
        return new ProjectResponseDto(project);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        projectRepository.delete(getProject(id));
    }

    private Project getProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다. id=" + id));
    }
}
