package com.portfolio.service;

import com.portfolio.domain.TechStack;
import com.portfolio.dto.TechStackResponseDto;

import java.util.List;
import java.util.Set;

public interface TechStackService {

    List<TechStackResponseDto> findAll();

    Set<TechStack> resolve(List<String> names);
}
