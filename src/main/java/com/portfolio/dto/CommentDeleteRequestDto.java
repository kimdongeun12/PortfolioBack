package com.portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 삭제(숨김) 요청 DTO
 */
@Getter
@NoArgsConstructor
public class CommentDeleteRequestDto {

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
