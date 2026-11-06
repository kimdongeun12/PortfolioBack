package com.portfolio.domain.image.controller;

import com.portfolio.auth.AdminAuthService;
import com.portfolio.auth.AdminCookieManager;
import com.portfolio.auth.AdminOnly;
import com.portfolio.domain.image.dto.ImageResponseDto;
import com.portfolio.domain.image.entity.ImageTargetType;
import com.portfolio.domain.image.service.ImageService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final AdminAuthService adminAuthService;
    private final AdminCookieManager adminCookieManager;

    @AdminOnly
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageResponseDto> upload(@RequestPart("file") MultipartFile file,
                                                   @RequestParam ImageTargetType targetType,
                                                   @RequestParam Long targetId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(imageService.upload(file, targetType, targetId));
    }

    @GetMapping
    public ResponseEntity<List<ImageResponseDto>> findByTarget(@RequestParam ImageTargetType targetType,
                                                               @RequestParam Long targetId,
                                                               HttpServletRequest request) {
        boolean admin = adminAuthService.isAuthenticated(adminCookieManager.resolveToken(request));
        return ResponseEntity.ok(imageService.findByTarget(targetType, targetId, admin));
    }

    @AdminOnly
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        imageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
