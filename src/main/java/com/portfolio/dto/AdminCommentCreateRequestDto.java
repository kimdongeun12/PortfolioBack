package com.portfolio.dto;

import com.portfolio.domain.CommentTargetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [관리자] 댓글 작성 요청 DTO (닉네임·비밀번호 없음)
 * - 최상위 댓글: targetType, targetId 필수
 * - 답글: parentId 만 있으면 됨
 */
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
