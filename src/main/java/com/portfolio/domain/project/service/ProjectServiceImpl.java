package com.portfolio.domain.project.service;

import com.portfolio.global.common.PageResponseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.portfolio.domain.experience.entity.Experience;
import com.portfolio.domain.experience.repository.ExperienceRepository;
import com.portfolio.domain.project.dto.ProjectRequestDto;
import com.portfolio.domain.project.dto.ProjectResponseDto;
import com.portfolio.domain.project.entity.Project;
import com.portfolio.domain.project.entity.ProjectType;
import com.portfolio.domain.project.repository.ProjectRepository;
import com.portfolio.domain.techstack.service.TechStackService;
import com.portfolio.global.exception.BadRequestException;
import com.portfolio.global.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private static final int MAX_PAGE_SIZE = 50;

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
        applyCover(project, requestDto);
        project.changeTechStacks(techStackService.resolve(requestDto.getTechStacks()));
        return projectRepository.save(project).getId();
    }

    @Override
    @Transactional
    public ProjectResponseDto update(Long id, ProjectRequestDto requestDto) {
        validatePeriod(requestDto);
        Project project = getProject(id);
        Experience experience = getExperienceOrNull(requestDto.getExperienceId());
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
        applyCover(project, requestDto);
        project.changeTechStacks(techStackService.resolve(requestDto.getTechStacks()));
        return new ProjectResponseDto(project);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getProject(id).delete();
    }

    @Override
    public PageResponseDto<ProjectResponseDto> findPage(int page, int size) {
        Sort sort = Sort.by(Sort.Order.desc("isPinned"), Sort.Order.asc("sortOrder"), Sort.Order.desc("startDate"));
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), MAX_PAGE_SIZE), sort);
        return new PageResponseDto<>(projectRepository.findAllByIsDeletedFalse(pageable), ProjectResponseDto::new);
    }

    @Override
    @Transactional
    public void reorder(List<Long> ids) {
        Map<Long, Project> projects = new HashMap<>();
        for (Project project : projectRepository.findAllById(ids)) {
            if (!project.isDeleted()) {
                projects.put(project.getId(), project);
            }
        }
        if (projects.size() != new HashSet<>(ids).size()) {
            throw new BadRequestException("정렬할 프로젝트 중 찾을 수 없는 항목이 있습니다.");
        }
        for (int i = 0; i < ids.size(); i++) {
            projects.get(ids.get(i)).changeSortOrder(i + 1);
        }
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

    private void applyCover(Project project, ProjectRequestDto requestDto) {
        project.changeCover(
                blankToNull(requestDto.getCoverLabel()),
                blankToNull(requestDto.getCoverTitle()),
                blankToNull(requestDto.getCoverSub())
        );
        project.changePeriodLabel(blankToNull(requestDto.getPeriodLabel()));
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
