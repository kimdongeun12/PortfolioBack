package com.portfolio.domain.blog.entity;

import com.portfolio.domain.techstack.entity.TechStack;
import com.portfolio.global.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

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

    public void changeTechStacks(Set<TechStack> techStacks) {
        this.techStacks.clear();
        this.techStacks.addAll(techStacks);
    }

    public void delete() {
        this.isDeleted = true;
    }
}
