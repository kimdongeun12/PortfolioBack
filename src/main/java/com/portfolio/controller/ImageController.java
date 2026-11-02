package com.portfolio.controller;

import com.portfolio.domain.ImageTargetType;
import com.portfolio.dto.ImageResponseDto;
import com.portfolio.service.ImageService;
import com.portfolio.auth.AdminOnly;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 이미지 REST API
 *
 * POST /api/images                                 업로드 (multipart/form-data: file, targetType, targetId)
 * GET  /api/images?targetType=BLOG&targetId=1      대상의 이미지 목록
 * POST /api/images/{id}/delete                     삭제 (숨김 처리)
 *
 * [관리자 전용] 업로드 · 삭제
 *
 * 업로드된 파일은 GET {백엔드주소}/uploads/... 로 접근합니다.
 */
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @AdminOnly
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageResponseDto> upload(@RequestPart("file") MultipartFile file,
                                                   @RequestParam ImageTargetType targetType,
                                                   @RequestParam Long targetId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.upload(file, targetType, targetId));
    }

    @GetMapping
    public ResponseEntity<List<ImageResponseDto>> findByTarget(@RequestParam ImageTargetType targetType,
                                                               @RequestParam Long targetId) {
        return ResponseEntity.ok(imageService.findByTarget(targetType, targetId));
    }

    @AdminOnly
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        imageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
