package com.portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 수정 요청 DTO (작성 시 입력한 비밀번호 확인)
 */
@Getter
@NoArgsConstructor
public class CommentUpdateRequestDto {

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "내용은 필수입니다.")
    @Size(max = 1000, message = "댓글은 1000자 이하로 입력하세요.")
    private String content;
}
