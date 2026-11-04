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
