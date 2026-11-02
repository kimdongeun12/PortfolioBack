package com.portfolio.dto;

import lombok.Getter;

/**
 * 좋아요 응답 DTO
 * alreadyLiked 가 true 면 이미 누른 상태라 숫자가 늘지 않은 것입니다.
 */
@Getter
public class LikeResponseDto {

    private final int likeCount;
    private final boolean liked;
    private final boolean alreadyLiked;

    public LikeResponseDto(int likeCount, boolean alreadyLiked) {
        this.likeCount = likeCount;
        this.liked = true;
        this.alreadyLiked = alreadyLiked;
    }
}
