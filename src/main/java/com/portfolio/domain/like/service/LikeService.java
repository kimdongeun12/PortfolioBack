package com.portfolio.domain.like.service;

import com.portfolio.domain.like.dto.LikeResponseDto;
import com.portfolio.domain.like.entity.LikeTargetType;

public interface LikeService {

    LikeResponseDto like(LikeTargetType targetType, Long targetId, String ipHash);
}
