package com.portfolio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 방문 기록 요청 DTO (프론트에서 페이지 이동 시 전송)
 * IP·User-Agent 는 서버에서 직접 꺼내므로 받지 않습니다.
 */
@Getter
@NoArgsConstructor
public class VisitRequestDto {

    /** 브라우저 세션 UUID (sessionStorage 에 저장한 값) */
    @NotBlank(message = "sessionId 는 필수입니다.")
    @Size(max = 36, message = "sessionId 는 36자 이하입니다.")
    private String sessionId;

    /** 방문 경로 (예: /blog/12) */
    @NotBlank(message = "path 는 필수입니다.")
    private String path;

    /** document.referrer (없으면 null) */
    private String referrer;
}
