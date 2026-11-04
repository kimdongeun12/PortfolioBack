package com.portfolio.domain;

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

    @Column(length = 500)
    private String userAgent;

    @Column(columnDefinition = "CHAR(64)")
    private String ipHash;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime visitedAt;

    @Builder
    public VisitLog(String sessionId, String path, String referrer, String userAgent, String ipHash) {
        this.sessionId = sessionId;
        this.path = path;
        this.referrer = referrer;
        this.userAgent = userAgent;
        this.ipHash = ipHash;
    }
}
