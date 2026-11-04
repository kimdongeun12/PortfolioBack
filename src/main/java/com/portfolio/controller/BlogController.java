package com.portfolio.controller;

import com.portfolio.domain.LikeTargetType;
import com.portfolio.dto.BlogRequestDto;
import com.portfolio.dto.BlogResponseDto;
import com.portfolio.dto.BlogSummaryDto;
import com.portfolio.dto.LikeResponseDto;
import com.portfolio.dto.PageResponseDto;
import com.portfolio.service.BlogService;
import com.portfolio.service.LikeService;
import com.portfolio.util.ClientInfoResolver;
import jakarta.servlet.http.HttpServletRequest;
import com.portfolio.auth.AdminOnly;
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
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(blogService.findPublished(tech, pageable));
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
