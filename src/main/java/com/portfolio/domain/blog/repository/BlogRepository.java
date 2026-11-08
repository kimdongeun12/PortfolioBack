package com.portfolio.domain.blog.repository;

import java.util.List;
import com.portfolio.domain.blog.entity.Blog;

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

    @Query(value = "select b from Blog b where b.isPublished = true and b.isDeleted = false and (:tech is null or b.id in (select b2.id from Blog b2 join b2.techStacks t where t.name = :tech)) and (:q is null or lower(b.title) like :q or lower(b.content) like :q)",
            countQuery = "select count(b) from Blog b where b.isPublished = true and b.isDeleted = false and (:tech is null or b.id in (select b2.id from Blog b2 join b2.techStacks t where t.name = :tech)) and (:q is null or lower(b.title) like :q or lower(b.content) like :q)")
    Page<Blog> searchPublished(@Param("tech") String tech, @Param("q") String q, Pageable pageable);

    long countByIsPublishedTrueAndIsDeletedFalse();

    @Query("select coalesce(sum(b.likeCount), 0) from Blog b where b.isPublished = true and b.isDeleted = false")
    long sumPublishedLikeCount();

    @Query("select b.id from Blog b where b.isPublished = true and b.isDeleted = false")
    List<Long> findPublishedIds();

    @Query("select distinct t.name from Blog b join b.techStacks t where b.isPublished = true and b.isDeleted = false")
    List<String> findPublishedTechNames();

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
