package com.portfolio.domain.visit.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "visit_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class VisitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 36)
    private String sessionId;

    @Column(nullable = false, length = 500)
    private String path;

    @Column(length = 500)
    private String referrer;

    @Column(length = 20)
    private String device;

    @Column(length = 20)
    private String os;

    @Column(length = 20)
    private String browser;

    @Column(nullable = false)
    private boolean botLike;

    @Column(columnDefinition = "CHAR(64)")
    private String ipHash;

    private Integer durationMs;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime visitedAt;

    @Builder
    public VisitLog(String sessionId, String path, String referrer, String device, String os, String browser, String ipHash) {
        this.sessionId = sessionId;
        this.path = path;
        this.referrer = referrer;
        this.device = device;
        this.os = os;
        this.browser = browser;
        this.ipHash = ipHash;
    }

    public void updateDuration(int durationMs) {
        if (this.durationMs == null || durationMs > this.durationMs) {
            this.durationMs = durationMs;
        }
    }
}
