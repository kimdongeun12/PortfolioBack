package com.portfolio.domain.like.repository;

import com.portfolio.domain.like.entity.LikeTargetType;
import com.portfolio.domain.like.entity.Likes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    boolean existsByTargetTypeAndTargetIdAndIpHash(LikeTargetType targetType, Long targetId, String ipHash);
}
