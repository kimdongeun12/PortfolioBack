package com.portfolio.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 기술 스택 (tech_stack 테이블) — 프로젝트·블로그가 함께 사용
 */
@Entity
@Table(name = "tech_stack")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

    public TechStack(String name) {
        this.name = name;
    }
}
