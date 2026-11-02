package com.portfolio.controller;

import com.portfolio.domain.CommentTargetType;
import com.portfolio.domain.LikeTargetType;
import com.portfolio.dto.CommentCreateRequestDto;
import com.portfolio.dto.CommentDeleteRequestDto;
import com.portfolio.dto.CommentResponseDto;
import com.portfolio.dto.CommentUpdateRequestDto;
import com.portfolio.dto.LikeResponseDto;
import com.portfolio.service.CommentService;
import com.portfolio.service.LikeService;
import com.portfolio.util.ClientInfoResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 댓글 REST API (블로그·프로젝트 공용)
 *
 * GET  /api/comments?targetType=BLOG&targetId=1   목록 (대댓글 중첩)
 * POST /api/comments                              작성 (대댓글은 parentId 포함)
 * PUT  /api/comments/{id}                         수정 (비밀번호 필요)
 * POST /api/comments/{id}/delete                  삭제 — 숨김 처리 (비밀번호 필요)
 * POST /api/comments/{id}/like                    좋아요 (IP 당 1회)
 */
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final LikeService likeService;
    private final ClientInfoResolver clientInfoResolver;

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> findByTarget(@RequestParam CommentTargetType targetType,
                                                                 @RequestParam Long targetId) {
        return ResponseEntity.ok(commentService.findByTarget(targetType, targetId));
    }

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody CommentCreateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(requestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> update(@PathVariable Long id,
                                                     @Valid @RequestBody CommentUpdateRequestDto requestDto) {
        return ResponseEntity.ok(commentService.update(id, requestDto.getPassword(), requestDto.getContent()));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @Valid @RequestBody CommentDeleteRequestDto requestDto) {
        commentService.delete(id, requestDto.getPassword());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<LikeResponseDto> like(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(likeService.like(LikeTargetType.COMMENT, id, clientInfoResolver.getIpHash(request)));
    }
}
