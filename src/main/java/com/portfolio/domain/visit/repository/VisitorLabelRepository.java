package com.portfolio.domain.visit.repository;

import com.portfolio.domain.visit.entity.VisitorLabel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorLabelRepository extends JpaRepository<VisitorLabel, String> {
}
