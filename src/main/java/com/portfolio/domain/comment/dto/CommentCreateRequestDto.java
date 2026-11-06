package com.portfolio.domain.comment.dto;

import com.portfolio.domain.comment.entity.CommentTargetType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
