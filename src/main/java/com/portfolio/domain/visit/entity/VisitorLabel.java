package com.portfolio.domain.visit.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "visitor_label")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VisitorLabel {

    @Id
    @Column(columnDefinition = "CHAR(64)")
    private String ipHash;

    @Column(nullable = false, length = 40)
    private String label;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public VisitorLabel(String ipHash, String label) {
        this.ipHash = ipHash;
        this.label = label;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void changeLabel(String label) {
        this.label = label;
        this.updatedAt = LocalDateTime.now();
    }
}
