package com.portfolio.service;

import com.portfolio.domain.LikeTargetType;
import com.portfolio.dto.LikeResponseDto;

public interface LikeService {

    LikeResponseDto like(LikeTargetType targetType, Long targetId, String ipHash);
}
