package com.portfolio.domain.skill.repository;

import com.portfolio.domain.skill.entity.SkillGroup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkillGroupRepository extends JpaRepository<SkillGroup, Long> {

    List<SkillGroup> findAllByIsDeletedFalseOrderBySortOrderAscIdAsc();

    Optional<SkillGroup> findByIdAndIsDeletedFalse(Long id);
}
