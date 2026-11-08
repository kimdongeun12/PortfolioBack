package com.portfolio.domain.blog.controller;

import com.portfolio.domain.blog.dto.BlogOverviewDto;
import com.portfolio.auth.AdminOnly;
import com.portfolio.domain.blog.dto.BlogRequestDto;
import com.portfolio.domain.blog.dto.BlogResponseDto;
import com.portfolio.domain.blog.dto.BlogSummaryDto;
import com.portfolio.domain.blog.service.BlogService;
import com.portfolio.domain.like.dto.LikeResponseDto;
import com.portfolio.domain.like.entity.LikeTargetType;
import com.portfolio.domain.like.service.LikeService;
import com.portfolio.global.common.PageResponseDto;
import com.portfolio.global.util.ClientInfoResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;
    private final LikeService likeService;
    private final ClientInfoResolver clientInfoResolver;

    @GetMapping
    public ResponseEntity<PageResponseDto<BlogSummaryDto>> findAll(
            @RequestParam(required = false) String tech,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(blogService.findPublished(tech, q, pageable));
    }

    @GetMapping("/overview")
    public ResponseEntity<BlogOverviewDto> overview() {
        return ResponseEntity.ok(blogService.getOverview());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogResponseDto> findById(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(blogService.findPublishedById(id, clientInfoResolver.getIpHash(request)));
    }

    @AdminOnly
    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody BlogRequestDto requestDto) {
        Long id = blogService.create(requestDto);
        return ResponseEntity.created(URI.create("/api/blogs/" + id)).body(id);
    }

    @AdminOnly
    @PutMapping("/{id}")
    public ResponseEntity<BlogResponseDto> update(@PathVariable Long id,
                                                  @Valid @RequestBody BlogRequestDto requestDto) {
        return ResponseEntity.ok(blogService.update(id, requestDto));
    }

    @AdminOnly
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        blogService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<LikeResponseDto> like(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(likeService.like(LikeTargetType.BLOG, id, clientInfoResolver.getIpHash(request)));
    }
}
