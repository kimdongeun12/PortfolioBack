package com.portfolio.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public void delete() {
        this.isDeleted = true;
    }

    public boolean isReply() {
        return parentId != null;
    }
}
