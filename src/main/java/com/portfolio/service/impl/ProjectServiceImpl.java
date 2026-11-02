package com.portfolio.service.impl;

import com.portfolio.domain.Experience;
import com.portfolio.domain.Project;
import com.portfolio.domain.ProjectType;
import com.portfolio.dto.ProjectRequestDto;
import com.portfolio.dto.ProjectResponseDto;
import com.portfolio.exception.BadRequestException;
import com.portfolio.exception.NotFoundException;
import com.portfolio.repository.ExperienceRepository;
import com.portfolio.repository.ProjectRepository;
import com.portfolio.service.ProjectService;
import com.portfolio.service.TechStackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ExperienceRepository experienceRepository;
    private final TechStackService techStackService;

    @Override
    public List<ProjectResponseDto> findAll(ProjectType type) {
        List<Project> projects = (type == null)
                ? projectRepository.findAllByIsDeletedFalseOrderByIsPinnedDescSortOrderAscStartDateDesc()
                : projectRepository.findAllByTypeAndIsDeletedFalseOrderByIsPinnedDescSortOrderAscStartDateDesc(type);
        return projects.stream()
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
        validatePeriod(requestDto);
        Experience experience = getExperienceOrNull(requestDto.getExperienceId());
        Project project = requestDto.toEntity(experience);
        project.changeTechStacks(techStackService.resolve(requestDto.getTechStacks()));
        return projectRepository.save(project).getId();
    }

    @Override
    @Transactional
    public ProjectResponseDto update(Long id, ProjectRequestDto requestDto) {
        validatePeriod(requestDto);
        Project project = getProject(id);
        Experience experience = getExperienceOrNull(requestDto.getExperienceId());
        // save() 호출 없이 변경 감지(dirty checking)로 UPDATE 됩니다.
        project.update(
                experience,
                requestDto.getTitle(),
                requestDto.getSummary(),
                requestDto.getRole(),
                requestDto.getResponsibilities(),
                requestDto.getContent(),
                requestDto.getType(),
                requestDto.getThumbnailUrl(),
                requestDto.isPinned(),
                requestDto.getSortOrder(),
                requestDto.getStartDate(),
                requestDto.getEndDate()
        );
        project.changeTechStacks(techStackService.resolve(requestDto.getTechStacks()));
        return new ProjectResponseDto(project);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 숨김 처리만 합니다. 댓글·이미지는 프로젝트가 안 보이면 함께 노출되지 않습니다.
        getProject(id).delete();
    }

    private Project getProject(Long id) {
        return projectRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("프로젝트를 찾을 수 없습니다. id=" + id));
    }

    private Experience getExperienceOrNull(Long experienceId) {
        if (experienceId == null) {
            return null;
        }
        return experienceRepository.findByIdAndIsDeletedFalse(experienceId)
                .orElseThrow(() -> new NotFoundException("경력을 찾을 수 없습니다. id=" + experienceId));
    }

    private void validatePeriod(ProjectRequestDto requestDto) {
        if (requestDto.getStartDate() != null && requestDto.getEndDate() != null
                && requestDto.getEndDate().isBefore(requestDto.getStartDate())) {
            throw new BadRequestException("종료일은 시작일보다 빠를 수 없습니다.");
        }
    }
}
