package com.portfolio.service.impl;

import com.portfolio.domain.Experience;
import com.portfolio.dto.ExperienceRequestDto;
import com.portfolio.dto.ExperienceResponseDto;
import com.portfolio.exception.BadRequestException;
import com.portfolio.exception.NotFoundException;
import com.portfolio.repository.ExperienceRepository;
import com.portfolio.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;

    @Override
    public List<ExperienceResponseDto> findAll() {
        return experienceRepository.findAllByIsDeletedFalseOrderByStartDateDesc().stream()
                .map(ExperienceResponseDto::new)
                .toList();
    }

    @Override
    public ExperienceResponseDto findById(Long id) {
        return new ExperienceResponseDto(getExperience(id));
    }

    @Override
    @Transactional
    public Long create(ExperienceRequestDto requestDto) {
        validatePeriod(requestDto);
        return experienceRepository.save(requestDto.toEntity()).getId();
    }

    @Override
    @Transactional
    public ExperienceResponseDto update(Long id, ExperienceRequestDto requestDto) {
        validatePeriod(requestDto);
        Experience experience = getExperience(id);
        experience.update(
                requestDto.getCompanyName(),
                requestDto.getFormerName(),
                requestDto.getDepartment(),
                requestDto.getJobTitle(),
                requestDto.getRole(),
                requestDto.getPosition(),
                requestDto.getResponsibilities(),
                requestDto.getAchievements(),
                requestDto.getStartDate(),
                requestDto.getEndDate()
        );
        return new ExperienceResponseDto(experience);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getExperience(id).delete();
    }

    private Experience getExperience(Long id) {
        return experienceRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("경력을 찾을 수 없습니다. id=" + id));
    }

    private void validatePeriod(ExperienceRequestDto requestDto) {
        if (requestDto.getEndDate() != null && requestDto.getEndDate().isBefore(requestDto.getStartDate())) {
            throw new BadRequestException("퇴사일은 입사일보다 빠를 수 없습니다.");
        }
    }
}
