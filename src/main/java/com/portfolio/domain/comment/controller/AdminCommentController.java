package com.portfolio.domain.comment.controller;

import com.portfolio.auth.AdminOnly;
import com.portfolio.domain.comment.dto.AdminCommentCreateRequestDto;
import com.portfolio.domain.comment.dto.AdminCommentUpdateRequestDto;
import com.portfolio.domain.comment.dto.CommentResponseDto;
import com.portfolio.domain.comment.service.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
