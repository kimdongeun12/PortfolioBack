package com.portfolio.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 포트폴리오 프로젝트 엔티티 (MySQL project 테이블과 매핑)
 * TODO: 필요한 컬럼을 추가하세요. (예: 썸네일, 기술 스택, 기간, 링크 등)
 */
@Entity
@Table(name = "project")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Project(String title, String description) {
        this.title = title;
        this.description = description;
    }

    /** 수정 로직은 엔티티 안에 두고, Service에서 호출합니다. (JPA 변경 감지) */
    public void update(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
