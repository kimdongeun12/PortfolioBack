package com.portfolio.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminLoginRequestDto {

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(max = 200, message = "비밀번호가 너무 깁니다.")
    private String password;
}
