package com.portfolio.domain.experience.entity;

import com.portfolio.global.common.BaseTimeEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "experience")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Experience extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String companyName;

    @Column(length = 100)
    private String formerName;

    @Column(length = 100)
    private String department;

    @Column(length = 100)
    private String jobTitle;

    @Column(length = 200)
    private String role;

    @Column(length = 50)
    private String position;

    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(columnDefinition = "TEXT")
    private String achievements;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    public Experience(String companyName, String formerName, String department, String jobTitle,
                      String role, String position, String responsibilities, String achievements,
                      LocalDate startDate, LocalDate endDate) {
        this.companyName = companyName;
        this.formerName = formerName;
        this.department = department;
        this.jobTitle = jobTitle;
        this.role = role;
        this.position = position;
        this.responsibilities = responsibilities;
        this.achievements = achievements;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void update(String companyName, String formerName, String department, String jobTitle,
                       String role, String position, String responsibilities, String achievements,
                       LocalDate startDate, LocalDate endDate) {
        this.companyName = companyName;
        this.formerName = formerName;
        this.department = department;
        this.jobTitle = jobTitle;
        this.role = role;
        this.position = position;
        this.responsibilities = responsibilities;
        this.achievements = achievements;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public boolean isCurrent() {
        return endDate == null;
    }
}
