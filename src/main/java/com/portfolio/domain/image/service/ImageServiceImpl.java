package com.portfolio.domain.image.service;

import com.portfolio.domain.blog.repository.BlogRepository;
import com.portfolio.domain.image.dto.ImageResponseDto;
import com.portfolio.domain.image.entity.Image;
import com.portfolio.domain.image.entity.ImageTargetType;
import com.portfolio.domain.image.repository.ImageRepository;
import com.portfolio.domain.project.repository.ProjectRepository;
import com.portfolio.global.exception.BadRequestException;
import com.portfolio.global.exception.NotFoundException;
import com.portfolio.global.util.ClientInfoResolver;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService {

    private static final Map<String, String> ALLOWED_TYPES = Map.of(
            "image/png", "png",
            "image/jpeg", "jpg",
            "image/gif", "gif",
            "image/webp", "webp"
    );

    private final ImageRepository imageRepository;
    private final ProjectRepository projectRepository;
    private final BlogRepository blogRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.url-prefix}")
    private String uploadUrlPrefix;

    @Override
    @Transactional
    public ImageResponseDto upload(MultipartFile file, ImageTargetType targetType, Long targetId) {
        validateTarget(targetType, targetId);

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("업로드할 파일이 없습니다.");
        }
        String contentType = file.getContentType();
        String extension = (contentType == null) ? null : ALLOWED_TYPES.get(contentType);
        if (extension == null) {
            throw new BadRequestException("png, jpg, gif, webp 이미지만 업로드할 수 있습니다.");
        }

        LocalDate today = LocalDate.now();
        String subDir = String.format("%d/%02d", today.getYear(), today.getMonthValue());
        String fileName = UUID.randomUUID() + "." + extension;

        Path baseDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path targetDir = baseDir.resolve(subDir);
        Path targetFile = targetDir.resolve(fileName);
        try {
            Files.createDirectories(targetDir);
            file.transferTo(targetFile);
        } catch (IOException e) {
            throw new IllegalStateException("이미지 저장에 실패했습니다.", e);
        }

        Image image = Image.builder()
                .targetType(targetType)
                .targetId(targetId)
                .url(uploadUrlPrefix + "/" + subDir + "/" + fileName)
                .originalName(ClientInfoResolver.truncate(file.getOriginalFilename(), 255))
                .mimeType(file.getContentType())
                .fileSize(file.getSize())
                .build();
        return new ImageResponseDto(imageRepository.save(image));
    }

    @Override
    public List<ImageResponseDto> findByTarget(ImageTargetType targetType, Long targetId, boolean includeUnpublished) {
        if (includeUnpublished) {
            validateTarget(targetType, targetId);
        } else {
            validatePublicTarget(targetType, targetId);
        }
        return imageRepository.findByTargetTypeAndTargetIdAndIsDeletedFalseOrderByIdAsc(targetType, targetId).stream()
                .map(ImageResponseDto::new)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Image image = imageRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없습니다. id=" + id));
        image.delete();
    }

    private void validateTarget(ImageTargetType targetType, Long targetId) {
        boolean exists = switch (targetType) {
            case PROJECT -> projectRepository.existsByIdAndIsDeletedFalse(targetId);
            case BLOG -> blogRepository.existsByIdAndIsDeletedFalse(targetId);
        };
        if (!exists) {
            throw new NotFoundException("이미지 대상을 찾을 수 없습니다. " + targetType + " id=" + targetId);
        }
    }

    private void validatePublicTarget(ImageTargetType targetType, Long targetId) {
        boolean exists = switch (targetType) {
            case PROJECT -> projectRepository.existsByIdAndIsDeletedFalse(targetId);
            case BLOG -> blogRepository.existsByIdAndIsPublishedTrueAndIsDeletedFalse(targetId);
        };
        if (!exists) {
            throw new NotFoundException("이미지 대상을 찾을 수 없습니다. " + targetType + " id=" + targetId);
        }
    }
}
