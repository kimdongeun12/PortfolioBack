package com.portfolio.service;

import com.portfolio.dto.ExperienceRequestDto;
import com.portfolio.dto.ExperienceResponseDto;

import java.util.List;

public interface ExperienceService {

    List<ExperienceResponseDto> findAll();

    ExperienceResponseDto findById(Long id);

    Long create(ExperienceRequestDto requestDto);

    ExperienceResponseDto update(Long id, ExperienceRequestDto requestDto);

    void delete(Long id);
}
