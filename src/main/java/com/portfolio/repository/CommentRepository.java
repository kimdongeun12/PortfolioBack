package com.portfolio.repository;

import com.portfolio.domain.Comment;
import com.portfolio.domain.CommentTargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /** 대상의 댓글 전체 (숨김 포함 — 대댓글 구조를 만들 때 필요), 작성순 */
    List<Comment> findByTargetTypeAndTargetIdOrderByCreatedAtAscIdAsc(CommentTargetType targetType, Long targetId);

    Optional<Comment> findByIdAndIsDeletedFalse(Long id);

    boolean existsByIdAndIsDeletedFalse(Long id);

    /** 좋아요 수 +1 */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Comment c set c.likeCount = c.likeCount + 1 where c.id = :id")
    int increaseLikeCount(@Param("id") Long id);

    @Query("select c.likeCount from Comment c where c.id = :id")
    int findLikeCountById(@Param("id") Long id);
}
