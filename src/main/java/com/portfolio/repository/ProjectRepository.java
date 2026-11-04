package com.portfolio.repository;

import com.portfolio.domain.Project;
import com.portfolio.domain.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByIsDeletedFalseOrderByIsPinnedDescSortOrderAscStartDateDesc();

    List<Project> findAllByTypeAndIsDeletedFalseOrderByIsPinnedDescSortOrderAscStartDateDesc(ProjectType type);

    Optional<Project> findByIdAndIsDeletedFalse(Long id);

    boolean existsByIdAndIsDeletedFalse(Long id);
}
