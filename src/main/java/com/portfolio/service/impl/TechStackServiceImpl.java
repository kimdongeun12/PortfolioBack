package com.portfolio.service.impl;

import com.portfolio.domain.TechStack;
import com.portfolio.dto.TechStackResponseDto;
import com.portfolio.exception.BadRequestException;
import com.portfolio.repository.TechStackRepository;
import com.portfolio.service.TechStackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TechStackServiceImpl implements TechStackService {

    private static final int MAX_NAME_LENGTH = 50;

    private final TechStackRepository techStackRepository;

    @Override
    public List<TechStackResponseDto> findAll() {
        return techStackRepository.findAllByOrderByNameAsc().stream()
                .map(TechStackResponseDto::new)
                .toList();
    }

    @Override
    @Transactional
    public Set<TechStack> resolve(List<String> names) {
        if (names == null || names.isEmpty()) {
            return new LinkedHashSet<>();
        }

        // 공백 제거 + 대소문자 무시 중복 제거 (입력 순서 유지)
        Map<String, String> normalized = new LinkedHashMap<>();
        for (String name : names) {
            if (name == null || name.isBlank()) {
                continue;
            }
            String trimmed = name.trim();
            if (trimmed.length() > MAX_NAME_LENGTH) {
                throw new BadRequestException("기술 스택 이름은 50자 이하로 입력하세요: " + trimmed);
            }
            normalized.putIfAbsent(trimmed.toLowerCase(Locale.ROOT), trimmed);
        }
        if (normalized.isEmpty()) {
            return new LinkedHashSet<>();
        }

        // DB 콜레이션(utf8mb4_unicode_ci)이 대소문자를 구분하지 않으므로 "react" 로 "React" 도 조회됩니다.
        Map<String, TechStack> existing = new LinkedHashMap<>();
        for (TechStack techStack : techStackRepository.findByNameIn(normalized.values())) {
            existing.put(techStack.getName().toLowerCase(Locale.ROOT), techStack);
        }

        Set<TechStack> result = new LinkedHashSet<>();
        normalized.forEach((key, name) -> {
            TechStack techStack = existing.get(key);
            if (techStack == null) {
                techStack = techStackRepository.save(new TechStack(name));
            }
            result.add(techStack);
        });
        return result;
    }
}
