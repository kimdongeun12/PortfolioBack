package com.portfolio.domain.skill.service;

import com.portfolio.domain.skill.dto.SkillGroupRequestDto;
import com.portfolio.domain.skill.dto.SkillGroupResponseDto;

import java.util.List;

public interface SkillGroupService {

    List<SkillGroupResponseDto> findAll();

    SkillGroupResponseDto findById(Long id);

    Long create(SkillGroupRequestDto requestDto);

    SkillGroupResponseDto update(Long id, SkillGroupRequestDto requestDto);

    void delete(Long id);
}
