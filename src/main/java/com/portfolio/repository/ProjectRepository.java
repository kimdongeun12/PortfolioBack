package com.portfolio.repository;

import com.portfolio.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    // TODO: 필요한 조회 메서드를 추가하세요. 예) List<Project> findByTitleContaining(String keyword);
}
