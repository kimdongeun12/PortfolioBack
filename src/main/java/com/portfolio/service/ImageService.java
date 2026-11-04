package com.portfolio.service;

import com.portfolio.domain.ImageTargetType;
import com.portfolio.dto.ImageResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    ImageResponseDto upload(MultipartFile file, ImageTargetType targetType, Long targetId);

    List<ImageResponseDto> findByTarget(ImageTargetType targetType, Long targetId);

    void delete(Long id);
}
