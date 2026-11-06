package com.portfolio.domain.techstack.controller;

import com.portfolio.domain.techstack.dto.TechStackResponseDto;
import com.portfolio.domain.techstack.service.TechStackService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
