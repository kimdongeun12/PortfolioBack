package com.portfolio.service;

import com.portfolio.domain.TechStack;
import com.portfolio.dto.TechStackResponseDto;

import java.util.List;
import java.util.Set;

/**
 * 기술 스택 비즈니스 로직 인터페이스
 */
public interface TechStackService {

    /** 전체 목록 (이름순) — 입력 자동완성용 */
    List<TechStackResponseDto> findAll();

    /**
     * 이름 목록을 기술 스택 엔티티로 변환합니다.
     * 이미 있는 이름은 재사용하고(대소문자 무시), 없는 이름은 새로 등록합니다.
     */
    Set<TechStack> resolve(List<String> names);
}
