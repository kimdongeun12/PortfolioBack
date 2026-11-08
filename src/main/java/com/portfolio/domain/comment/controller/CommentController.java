package com.portfolio.domain.comment.controller;

import com.portfolio.domain.comment.dto.CommentPageResponseDto;
import com.portfolio.domain.comment.dto.CommentCreateRequestDto;
import com.portfolio.domain.comment.dto.CommentDeleteRequestDto;
import com.portfolio.domain.comment.dto.CommentResponseDto;
import com.portfolio.domain.comment.dto.CommentUpdateRequestDto;
import com.portfolio.domain.comment.entity.CommentTargetType;
import com.portfolio.domain.comment.service.CommentService;
import com.portfolio.domain.like.dto.LikeResponseDto;
import com.portfolio.domain.like.entity.LikeTargetType;
import com.portfolio.domain.like.service.LikeService;
import com.portfolio.global.ratelimit.RateLimiter;
import com.portfolio.global.util.ClientInfoResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private static final String CREATE_LIMIT_MESSAGE = "댓글을 너무 자주 작성했습니다. 잠시 후 다시 시도하세요.";
    private static final String PASSWORD_LIMIT_MESSAGE = "비밀번호 확인 요청이 너무 많습니다. 잠시 후 다시 시도하세요.";

    private final CommentService commentService;
    private final LikeService likeService;
    private final ClientInfoResolver clientInfoResolver;
    private final RateLimiter rateLimiter;

    @GetMapping("/page")
    public ResponseEntity<CommentPageResponseDto> findThreadPage(@RequestParam CommentTargetType targetType,
                                                                 @RequestParam Long targetId,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.findThreadPage(targetType, targetId, page, size));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> findByTarget(@RequestParam CommentTargetType targetType,
                                                                 @RequestParam Long targetId) {
        return ResponseEntity.ok(commentService.findByTarget(targetType, targetId));
    }

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody CommentCreateRequestDto requestDto,
                                       HttpServletRequest request) {
        String ip = clientInfoResolver.getClientIp(request);
        rateLimiter.check("comment-create-min:" + ip, 3, Duration.ofMinutes(1), CREATE_LIMIT_MESSAGE);
        rateLimiter.check("comment-create-hour:" + ip, 20, Duration.ofHours(1), CREATE_LIMIT_MESSAGE);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(requestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> update(@PathVariable Long id,
                                                     @Valid @RequestBody CommentUpdateRequestDto requestDto,
                                                     HttpServletRequest request) {
        checkPasswordLimit(id, request);
        return ResponseEntity.ok(commentService.update(id, requestDto.getPassword(), requestDto.getContent()));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @Valid @RequestBody CommentDeleteRequestDto requestDto,
                                       HttpServletRequest request) {
        checkPasswordLimit(id, request);
        commentService.delete(id, requestDto.getPassword());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<LikeResponseDto> like(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(likeService.like(LikeTargetType.COMMENT, id, clientInfoResolver.getIpHash(request)));
    }

    private void checkPasswordLimit(Long commentId, HttpServletRequest request) {
        String ip = clientInfoResolver.getClientIp(request);
        rateLimiter.check("comment-password-ip:" + ip, 10, Duration.ofMinutes(10), PASSWORD_LIMIT_MESSAGE);
        rateLimiter.check("comment-password-id:" + commentId, 20, Duration.ofHours(1), PASSWORD_LIMIT_MESSAGE);
    }
}
