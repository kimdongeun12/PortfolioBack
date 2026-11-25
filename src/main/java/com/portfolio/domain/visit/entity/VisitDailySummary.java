package com.portfolio.domain.visit.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "visit_daily_summary")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VisitDailySummary {

    @Id
    private LocalDate visitDate;

    @Column(nullable = false)
    private int pageViews;

    @Column(nullable = false)
    private int visitors;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
