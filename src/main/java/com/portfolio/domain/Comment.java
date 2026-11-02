package com.portfolio.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 (comment 테이블) — 블로그·프로젝트 공용, 대댓글 1단계
 * - parentId 가 null 이면 최상위 댓글, 값이 있으면 대댓글
 * - 삭제는 isDeleted = true 로 숨김 처리 (대댓글은 유지)
 * - password 는 BCrypt 해시값
 * - isAdmin 이 true 면 어드민에서 작성한 댓글 (비밀번호로 수정·삭제 불가)
 */
@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentTargetType targetType;

    @Column(nullable = false)
    private Long targetId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean isAdmin;

    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    public Comment(CommentTargetType targetType, Long targetId, Long parentId,
                   String nickname, String password, String content, boolean isAdmin) {
        this.targetType = targetType;
        this.targetId = targetId;
        this.parentId = parentId;
        this.nickname = nickname;
        this.password = password;
        this.content = content;
        this.isAdmin = isAdmin;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    /** 숨김 처리 (물리 삭제하지 않음) */
    public void delete() {
        this.isDeleted = true;
    }

    public boolean isReply() {
        return parentId != null;
    }
}
