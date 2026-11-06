package com.portfolio.domain.comment.dto;

import com.portfolio.domain.comment.entity.CommentTargetType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminCommentCreateRequestDto {

    private CommentTargetType targetType;

    private Long targetId;

    private Long parentId;

    @NotBlank(message = "내용은 필수입니다.")
    @Size(max = 1000, message = "댓글은 1000자 이하로 입력하세요.")
    private String content;
}
