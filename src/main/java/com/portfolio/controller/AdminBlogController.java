package com.portfolio.controller;

import com.portfolio.auth.AdminOnly;
import com.portfolio.dto.BlogResponseDto;
import com.portfolio.dto.BlogSummaryDto;
import com.portfolio.dto.PageResponseDto;
import com.portfolio.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AdminOnly
@RestController
@RequestMapping("/api/admin/blogs")
@RequiredArgsConstructor
public class AdminBlogController {

    private final BlogService blogService;

    @GetMapping
    public ResponseEntity<PageResponseDto<BlogSummaryDto>> findAll(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(blogService.findAllForAdmin(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.findByIdForAdmin(id));
    }
}
