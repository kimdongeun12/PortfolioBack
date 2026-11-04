package com.portfolio.repository;

import com.portfolio.domain.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    List<Experience> findAllByIsDeletedFalseOrderByStartDateDesc();

    Optional<Experience> findByIdAndIsDeletedFalse(Long id);
}
