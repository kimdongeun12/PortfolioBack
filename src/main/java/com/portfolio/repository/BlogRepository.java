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

    Page<Blog> findByIsPublishedTrueAndIsDeletedFalse(Pageable pageable);

    Page<Blog> findDistinctByIsPublishedTrueAndIsDeletedFalseAndTechStacks_Name(String techName, Pageable pageable);

    Page<Blog> findByIsDeletedFalse(Pageable pageable);

    Optional<Blog> findByIdAndIsPublishedTrueAndIsDeletedFalse(Long id);

    Optional<Blog> findByIdAndIsDeletedFalse(Long id);

    boolean existsByIdAndIsDeletedFalse(Long id);

    boolean existsByIdAndIsPublishedTrueAndIsDeletedFalse(Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Blog b set b.likeCount = b.likeCount + 1 where b.id = :id")
    int increaseLikeCount(@Param("id") Long id);

    @Query("select b.likeCount from Blog b where b.id = :id")
    int findLikeCountById(@Param("id") Long id);
}
