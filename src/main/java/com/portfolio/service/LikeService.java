package com.portfolio.service;

import com.portfolio.domain.LikeTargetType;
import com.portfolio.dto.LikeResponseDto;

/**
 * 좋아요 비즈니스 로직 인터페이스
 * 같은 IP(해시)는 대상마다 1번만 누를 수 있고, 취소 기능은 없습니다.
 */
public interface LikeService {

    LikeResponseDto like(LikeTargetType targetType, Long targetId, String ipHash);
}
