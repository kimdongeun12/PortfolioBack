package com.portfolio.repository;

import com.portfolio.domain.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    /** 숨김 처리되지 않은 경력, 최신 입사일 순 */
    List<Experience> findAllByIsDeletedFalseOrderByStartDateDesc();

    Optional<Experience> findByIdAndIsDeletedFalse(Long id);
}
