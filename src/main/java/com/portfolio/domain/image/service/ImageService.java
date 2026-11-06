package com.portfolio.domain.image.service;

import com.portfolio.domain.image.dto.ImageResponseDto;
import com.portfolio.domain.image.entity.ImageTargetType;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    ImageResponseDto upload(MultipartFile file, ImageTargetType targetType, Long targetId);

    List<ImageResponseDto> findByTarget(ImageTargetType targetType, Long targetId, boolean includeUnpublished);

    void delete(Long id);
}
