package com.portfolio.repository;

import com.portfolio.domain.LikeTargetType;
import com.portfolio.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    boolean existsByTargetTypeAndTargetIdAndIpHash(LikeTargetType targetType, Long targetId, String ipHash);
}
