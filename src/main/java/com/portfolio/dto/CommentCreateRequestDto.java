package com.portfolio.dto;

import com.portfolio.domain.CommentTargetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 작성 요청 DTO
 * - 최상위 댓글: targetType, targetId 필수 / parentId 없음
 * - 대댓글: parentId 만 있으면 됨 (대상은 부모 댓글에서 가져옴)
 */
@Getter
@NoArgsConstructor
public class CommentCreateRequestDto {

    private CommentTargetType targetType;

    private Long targetId;

    private Long parentId;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 30, message = "닉네임은 30자 이하로 입력하세요.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 4, max = 30, message = "비밀번호는 4~30자로 입력하세요.")
    private String password;

    @NotBlank(message = "내용은 필수입니다.")
    @Size(max = 1000, message = "댓글은 1000자 이하로 입력하세요.")
    private String content;
}
