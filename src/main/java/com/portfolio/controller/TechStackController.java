package com.portfolio.controller;

import com.portfolio.dto.TechStackResponseDto;
import com.portfolio.service.TechStackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 기술 스택 REST API
 *
 * GET /api/tech-stacks  전체 목록 (이름순, 입력 자동완성용)
 *
 * 등록은 프로젝트/블로그 저장 시 techStacks 이름 목록으로 자동 처리됩니다.
 */
@RestController
@RequestMapping("/api/tech-stacks")
@RequiredArgsConstructor
public class TechStackController {

    private final TechStackService techStackService;

    @GetMapping
    public ResponseEntity<List<TechStackResponseDto>> findAll() {
        return ResponseEntity.ok(techStackService.findAll());
    }
}
