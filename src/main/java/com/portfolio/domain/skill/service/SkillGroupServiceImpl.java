package com.portfolio.domain.skill.service;

import com.portfolio.domain.skill.dto.SkillGroupRequestDto;
import com.portfolio.domain.skill.dto.SkillGroupResponseDto;
import com.portfolio.domain.skill.entity.SkillGroup;
import com.portfolio.domain.skill.repository.SkillGroupRepository;
import com.portfolio.domain.techstack.service.TechStackService;
import com.portfolio.global.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SkillGroupServiceImpl implements SkillGroupService {

    private final SkillGroupRepository skillGroupRepository;
    private final TechStackService techStackService;

    @Override
    public List<SkillGroupResponseDto> findAll() {
        return skillGroupRepository.findAllByIsDeletedFalseOrderBySortOrderAscIdAsc().stream()
                .map(SkillGroupResponseDto::new)
                .toList();
    }

    @Override
    public SkillGroupResponseDto findById(Long id) {
        return new SkillGroupResponseDto(getSkillGroup(id));
    }

    @Override
    @Transactional
    public Long create(SkillGroupRequestDto requestDto) {
        SkillGroup skillGroup = new SkillGroup(
                requestDto.getName().trim(),
                requestDto.getDescription(),
                requestDto.getSortOrder()
        );
        skillGroup.changeTechStacks(new ArrayList<>(techStackService.resolve(requestDto.getTechStacks())));
        return skillGroupRepository.save(skillGroup).getId();
    }

    @Override
    @Transactional
    public SkillGroupResponseDto update(Long id, SkillGroupRequestDto requestDto) {
        SkillGroup skillGroup = getSkillGroup(id);
        skillGroup.update(requestDto.getName().trim(), requestDto.getDescription(), requestDto.getSortOrder());
        skillGroup.changeTechStacks(new ArrayList<>(techStackService.resolve(requestDto.getTechStacks())));
        return new SkillGroupResponseDto(skillGroup);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getSkillGroup(id).delete();
    }

    private SkillGroup getSkillGroup(Long id) {
        return skillGroupRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("스킬 그룹을 찾을 수 없습니다. id=" + id));
    }
}
