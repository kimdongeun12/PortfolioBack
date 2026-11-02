package com.portfolio.dto;

import com.portfolio.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 댓글 응답 DTO (최상위 댓글 안에 대댓글 목록 포함)
 * 삭제된 댓글은 닉네임·내용을 가리고 deleted = true 로 내려줍니다.
 */
@Getter
public class CommentResponseDto {

    private static final String DELETED_CONTENT = "삭제된 댓글입니다.";

    private final Long id;
    private final Long parentId;
    private final String nickname;
    private final String content;
    private final int likeCount;
    /** 관리자(블로그 주인) 댓글 여부 — '작성자' 배지 표시용 */
    private final boolean admin;
    private final boolean deleted;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<CommentResponseDto> replies;

    public CommentResponseDto(Comment comment, List<CommentResponseDto> replies) {
        this.id = comment.getId();
        this.parentId = comment.getParentId();
        this.deleted = comment.isDeleted();
        this.nickname = deleted ? null : comment.getNickname();
        this.content = deleted ? DELETED_CONTENT : comment.getContent();
        this.likeCount = deleted ? 0 : comment.getLikeCount();
        this.admin = comment.isAdmin();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.replies = replies;
    }

    /** 대댓글용 (하위 목록 없음) */
    public CommentResponseDto(Comment comment) {
        this(comment, List.of());
    }
}
