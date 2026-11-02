package com.portfolio.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 업로드 이미지 정보 (image 테이블)
 * 파일 자체는 업로드 폴더에 저장하고, DB 에는 접근 URL 만 저장합니다.
 * 삭제는 isDeleted = true 로 숨김 처리 (파일은 남아있음)
 */
@Entity
@Table(name = "image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImageTargetType targetType;

    @Column(nullable = false)
    private Long targetId;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(length = 255)
    private String originalName;

    @Column(length = 50)
    private String mimeType;

    private Long fileSize;

    @Column(nullable = false)
    private boolean isDeleted;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Image(ImageTargetType targetType, Long targetId, String url,
                 String originalName, String mimeType, Long fileSize) {
        this.targetType = targetType;
        this.targetId = targetId;
        this.url = url;
        this.originalName = originalName;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
    }

    /** 숨김 처리 (물리 삭제하지 않음) */
    public void delete() {
        this.isDeleted = true;
    }
}
