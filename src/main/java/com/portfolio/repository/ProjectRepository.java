package com.portfolio.repository;

import com.portfolio.domain.Project;
import com.portfolio.domain.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    /** 고정(pin) 우선 → 정렬 순서 → 최신 시작일 순 (숨김 제외) */
    List<Project> findAllByIsDeletedFalseOrderByIsPinnedDescSortOrderAscStartDateDesc();

    /** 종류별 목록 (정렬 동일) */
    List<Project> findAllByTypeAndIsDeletedFalseOrderByIsPinnedDescSortOrderAscStartDateDesc(ProjectType type);

    Optional<Project> findByIdAndIsDeletedFalse(Long id);

    boolean existsByIdAndIsDeletedFalse(Long id);
}
