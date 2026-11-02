package com.portfolio.service;

import com.portfolio.domain.ImageTargetType;
import com.portfolio.dto.ImageResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 이미지 비즈니스 로직 인터페이스
 */
public interface ImageService {

    /** 업로드 (대상 블로그/프로젝트가 먼저 저장되어 있어야 함) */
    ImageResponseDto upload(MultipartFile file, ImageTargetType targetType, Long targetId);

    /** 대상의 이미지 목록 */
    List<ImageResponseDto> findByTarget(ImageTargetType targetType, Long targetId);

    /** 삭제 (숨김 처리) */
    void delete(Long id);
}
