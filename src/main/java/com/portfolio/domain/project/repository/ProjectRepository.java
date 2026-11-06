package com.portfolio.domain.project.repository;

import com.portfolio.domain.project.entity.Project;
import com.portfolio.domain.project.entity.ProjectType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByIsDeletedFalseOrderByIsPinnedDescSortOrderAscStartDateDesc();

    List<Project> findAllByTypeAndIsDeletedFalseOrderByIsPinnedDescSortOrderAscStartDateDesc(ProjectType type);

    Optional<Project> findByIdAndIsDeletedFalse(Long id);

    boolean existsByIdAndIsDeletedFalse(Long id);
}
