package com.portfolio.domain.techstack.service;

import com.portfolio.domain.techstack.dto.TechStackResponseDto;
import com.portfolio.domain.techstack.entity.TechStack;

import java.util.List;
import java.util.Set;

public interface TechStackService {

    List<TechStackResponseDto> findAll();

    Set<TechStack> resolve(List<String> names);
}
