package com.portfolio.domain.project.entity;

import com.portfolio.domain.experience.entity.Experience;
import com.portfolio.domain.techstack.entity.TechStack;
import com.portfolio.global.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @Column(length = 40)
    private String coverLabel;

    @Column(length = 60)
    private String coverTitle;

    @Column(length = 100)
    private String coverSub;

    @Column(length = 40)
    private String periodLabel;

    @Column(nullable = false)
    private boolean isPinned;

    @Column(nullable = false)
    private int sortOrder;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private boolean isDeleted;

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

    public void changePeriodLabel(String periodLabel) {
        this.periodLabel = periodLabel;
    }

    public void changeCover(String coverLabel, String coverTitle, String coverSub) {
        this.coverLabel = coverLabel;
        this.coverTitle = coverTitle;
        this.coverSub = coverSub;
    }

    public void changeTechStacks(Set<TechStack> techStacks) {
        this.techStacks.clear();
        this.techStacks.addAll(techStacks);
    }

    public void changeSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
