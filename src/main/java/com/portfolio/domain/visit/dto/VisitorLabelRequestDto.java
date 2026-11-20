package com.portfolio.domain.visit.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VisitorLabelRequestDto {

    @Size(max = 40, message = "이름은 40자 이하로 입력해주세요.")
    private String label;
}
