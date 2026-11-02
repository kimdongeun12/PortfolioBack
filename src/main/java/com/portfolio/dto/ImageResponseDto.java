package com.portfolio.dto;

import com.portfolio.domain.Image;
import com.portfolio.domain.ImageTargetType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ImageResponseDto {

    private final Long id;
    private final ImageTargetType targetType;
    private final Long targetId;
    /** 접근 경로 (예: /uploads/2026/11/xxxx.png) — 프론트에서 백엔드 주소를 앞에 붙여 사용 */
    private final String url;
    private final String originalName;
    private final String mimeType;
    private final Long fileSize;
    private final LocalDateTime createdAt;

    public ImageResponseDto(Image image) {
        this.id = image.getId();
        this.targetType = image.getTargetType();
        this.targetId = image.getTargetId();
        this.url = image.getUrl();
        this.originalName = image.getOriginalName();
        this.mimeType = image.getMimeType();
        this.fileSize = image.getFileSize();
        this.createdAt = image.getCreatedAt();
    }
}
