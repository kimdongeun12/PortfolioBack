package com.portfolio.domain.comment.repository;

import com.portfolio.domain.comment.entity.Comment;
import com.portfolio.domain.comment.entity.CommentTargetType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByTargetTypeAndTargetIdOrderByCreatedAtAscIdAsc(CommentTargetType targetType, Long targetId);

    Optional<Comment> findByIdAndIsDeletedFalse(Long id);

    boolean existsByIdAndIsDeletedFalse(Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Comment c set c.likeCount = c.likeCount + 1 where c.id = :id")
    int increaseLikeCount(@Param("id") Long id);

    @Query("select c.likeCount from Comment c where c.id = :id")
    int findLikeCountById(@Param("id") Long id);

    @Query("select c.targetId, count(c) from Comment c where c.targetType = :targetType and c.targetId in :targetIds and c.isDeleted = false group by c.targetId")
    List<Object[]> countByTargets(@Param("targetType") CommentTargetType targetType, @Param("targetIds") Collection<Long> targetIds);
}
