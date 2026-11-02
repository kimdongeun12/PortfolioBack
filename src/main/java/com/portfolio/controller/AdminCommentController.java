package com.portfolio.controller;

import com.portfolio.auth.AdminOnly;
import com.portfolio.dto.AdminCommentCreateRequestDto;
import com.portfolio.dto.AdminCommentUpdateRequestDto;
import com.portfolio.dto.CommentResponseDto;
import com.portfolio.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * [관리자 전용] 댓글 관리 API
 *
 * POST /api/admin/comments               관리자 댓글·답글 작성 (비밀번호 없음, '작성자' 표시)
 * PUT  /api/admin/comments/{id}          관리자 댓글 수정 (관리자가 쓴 댓글만)
 * POST /api/admin/comments/{id}/delete   아무 댓글이나 숨김 처리 (스팸·악성 댓글 정리용)
 *
 * 목록은 공개 API(GET /api/comments)를 그대로 사용합니다.
 */
@AdminOnly
@RestController
@RequestMapping("/api/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody AdminCommentCreateRequestDto requestDto) {
        Long id = commentService.createByAdmin(
                requestDto.getTargetType(), requestDto.getTargetId(), requestDto.getParentId(), requestDto.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> update(@PathVariable Long id,
                                                     @Valid @RequestBody AdminCommentUpdateRequestDto requestDto) {
        return ResponseEntity.ok(commentService.updateByAdmin(id, requestDto.getContent()));
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.deleteByAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
