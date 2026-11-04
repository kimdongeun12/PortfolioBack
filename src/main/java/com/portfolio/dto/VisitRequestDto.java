package com.portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VisitRequestDto {

    @NotBlank(message = "sessionId 는 필수입니다.")
    @Size(max = 36, message = "sessionId 는 36자 이하입니다.")
    private String sessionId;

    @NotBlank(message = "path 는 필수입니다.")
    private String path;

    private String referrer;
}
