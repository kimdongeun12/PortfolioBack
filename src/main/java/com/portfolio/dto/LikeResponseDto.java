package com.portfolio.dto;

import lombok.Getter;

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
