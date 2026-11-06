package com.portfolio.domain.skill.entity;

import com.portfolio.domain.techstack.entity.TechStack;
import com.portfolio.global.common.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "skill_group")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SkillGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 300)
    private String description;

    @Column(nullable = false)
    private int sortOrder;

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToMany
    @JoinTable(
            name = "skill_group_tech",
            joinColumns = @JoinColumn(name = "skill_group_id"),
            inverseJoinColumns = @JoinColumn(name = "tech_id")
    )
    @OrderColumn(name = "sort_order")
    private List<TechStack> techStacks = new ArrayList<>();

    public SkillGroup(String name, String description, int sortOrder) {
        this.name = name;
        this.description = description;
        this.sortOrder = sortOrder;
    }

    public void update(String name, String description, int sortOrder) {
        this.name = name;
        this.description = description;
        this.sortOrder = sortOrder;
    }

    public void changeTechStacks(List<TechStack> techStacks) {
        this.techStacks.clear();
        this.techStacks.addAll(techStacks);
    }

    public void delete() {
        this.isDeleted = true;
    }
}
