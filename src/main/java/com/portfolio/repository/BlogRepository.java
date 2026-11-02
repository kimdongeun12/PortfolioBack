package com.portfolio.repository;

import com.portfolio.domain.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    /** 공개 글 목록 (숨김·임시저장 제외) */
    Page<Blog> findByIsPublishedTrueAndIsDeletedFalse(Pageable pageable);

    /** 기술 스택 이름으로 필터한 공개 글 목록 */
    Page<Blog> findDistinctByIsPublishedTrueAndIsDeletedFalseAndTechStacks_Name(String techName, Pageable pageable);

    /** 관리자 목록 (임시저장 포함, 숨김 제외) */
    Page<Blog> findByIsDeletedFalse(Pageable pageable);

    /** 공개 상세 (공개 + 숨김 아님) */
    Optional<Blog> findByIdAndIsPublishedTrueAndIsDeletedFalse(Long id);

    /** 단건 (숨김 제외, 임시저장 포함 — 관리자 수정 화면용) */
    Optional<Blog> findByIdAndIsDeletedFalse(Long id);

    boolean existsByIdAndIsDeletedFalse(Long id);

    /** 댓글·좋아요 가능한 글 (공개 + 숨김 아님) */
    boolean existsByIdAndIsPublishedTrueAndIsDeletedFalse(Long id);

    /** 좋아요 수 +1 (동시 요청에도 안전하도록 DB 에서 직접 증가) */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Blog b set b.likeCount = b.likeCount + 1 where b.id = :id")
    int increaseLikeCount(@Param("id") Long id);

    @Query("select b.likeCount from Blog b where b.id = :id")
    int findLikeCountById(@Param("id") Long id);
}
