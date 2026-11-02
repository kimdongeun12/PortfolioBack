package com.portfolio.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 블로그 글 (blog 테이블)
 * - isPublished = false 는 임시저장 (목록에 노출 안 됨)
 * - 삭제는 isDeleted = true 로 숨김 처리
 * - likeCount 는 likes 테이블의 집계 캐시 (LikeService 에서 증가)
 */
@Entity
@Table(name = "blog")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(length = 500)
    private String thumbnailUrl;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private boolean isPublished;

    @Column(nullable = false)
    private boolean isDeleted;

    /** 기술 스택 (blog_tech 연결 테이블) */
    @ManyToMany
    @JoinTable(
            name = "blog_tech",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "tech_id")
    )
    private Set<TechStack> techStacks = new LinkedHashSet<>();

    @Builder
    public Blog(String title, String content, String thumbnailUrl, boolean isPublished) {
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.isPublished = isPublished;
    }

    public void update(String title, String content, String thumbnailUrl, boolean isPublished) {
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.isPublished = isPublished;
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
