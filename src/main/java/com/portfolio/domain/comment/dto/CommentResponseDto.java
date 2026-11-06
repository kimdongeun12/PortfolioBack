package com.portfolio.domain.comment.dto;

import com.portfolio.domain.comment.entity.Comment;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CommentResponseDto {

    private static final String DELETED_CONTENT = "삭제된 댓글입니다.";

    private final Long id;
    private final Long parentId;
    private final String nickname;
    private final String content;
    private final int likeCount;
    private final boolean admin;
    private final boolean deleted;
    private final boolean edited;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<CommentResponseDto> replies;

    public CommentResponseDto(Comment comment, List<CommentResponseDto> replies) {
        this.id = comment.getId();
        this.parentId = comment.getParentId();
        this.deleted = comment.isDeleted();
        this.edited = !deleted && comment.isEdited();
        this.nickname = deleted ? null : comment.getNickname();
        this.content = deleted ? DELETED_CONTENT : comment.getContent();
        this.likeCount = deleted ? 0 : comment.getLikeCount();
        this.admin = comment.isAdmin();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.replies = replies;
    }

    public CommentResponseDto(Comment comment) {
        this(comment, List.of());
    }
}
