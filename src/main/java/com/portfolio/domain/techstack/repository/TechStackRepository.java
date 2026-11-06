package com.portfolio.domain.techstack.repository;

import com.portfolio.domain.techstack.entity.TechStack;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TechStackRepository extends JpaRepository<TechStack, Long> {

    List<TechStack> findAllByOrderByNameAsc();

    List<TechStack> findByNameIn(Collection<String> names);
}
