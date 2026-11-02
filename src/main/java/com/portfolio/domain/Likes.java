package com.portfolio.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 좋아요 기록 (likes 테이블) — IP 해시 기준으로 대상마다 1번만 가능
 * ※ 클래스명을 Like 로 하면 JPQL 예약어(LIKE)와 겹쳐서 Likes 로 지었습니다.
 */
@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LikeTargetType targetType;

    @Column(nullable = false)
    private Long targetId;

    /** DB 타입이 CHAR(64) 라서 columnDefinition 을 맞춰줘야 validate 를 통과합니다. */
    @Column(nullable = false, columnDefinition = "CHAR(64)")
    private String ipHash;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public Likes(LikeTargetType targetType, Long targetId, String ipHash) {
        this.targetType = targetType;
        this.targetId = targetId;
        this.ipHash = ipHash;
    }
}
