package com.portfolio.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 포트폴리오 프로젝트 엔티티 (project 테이블)
 * - experience 가 null 이면 개인/사이드 프로젝트
 * - endDate 가 null 이면 진행중
 * - 삭제는 isDeleted = true 로 숨김 처리
 */
@Entity
@Table(name = "project")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experience_id")
    private Experience experience;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 300)
    private String summary;

    @Column(length = 200)
    private String role;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectType type;

    @Column(length = 500)
    private String thumbnailUrl;

    @Column(nullable = false)
    private boolean isPinned;

    @Column(nullable = false)
    private int sortOrder;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private boolean isDeleted;

    /** 기술 스택 (project_tech 연결 테이블) */
    @ManyToMany
    @JoinTable(
            name = "project_tech",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "tech_id")
    )
    private Set<TechStack> techStacks = new LinkedHashSet<>();

    @Builder
    public Project(Experience experience, String title, String summary, String role,
                   String responsibilities, String content, ProjectType type, String thumbnailUrl,
                   boolean isPinned, int sortOrder, LocalDate startDate, LocalDate endDate) {
        this.experience = experience;
        this.title = title;
        this.summary = summary;
        this.role = role;
        this.responsibilities = responsibilities;
        this.content = content;
        this.type = type;
        this.thumbnailUrl = thumbnailUrl;
        this.isPinned = isPinned;
        this.sortOrder = sortOrder;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /** 수정 로직은 엔티티 안에 두고, Service에서 호출합니다. (JPA 변경 감지) */
    public void update(Experience experience, String title, String summary, String role,
                       String responsibilities, String content, ProjectType type, String thumbnailUrl,
                       boolean isPinned, int sortOrder, LocalDate startDate, LocalDate endDate) {
        this.experience = experience;
        this.title = title;
        this.summary = summary;
        this.role = role;
        this.responsibilities = responsibilities;
        this.content = content;
        this.type = type;
        this.thumbnailUrl = thumbnailUrl;
        this.isPinned = isPinned;
        this.sortOrder = sortOrder;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /** 기술 스택 전체 교체 */
    public void changeTechStacks(Set<TechStack> techStacks) {
        this.techStacks.clear();
        this.techStacks.addAll(techStacks);
    }

    /** 숨김 처리 (물리 삭제하지 않음) */
    public void delete() {
        this.isDeleted = true;
    }
}
