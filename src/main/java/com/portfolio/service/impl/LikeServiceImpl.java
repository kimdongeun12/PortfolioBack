package com.portfolio.service.impl;

import com.portfolio.domain.LikeTargetType;
import com.portfolio.domain.Likes;
import com.portfolio.dto.LikeResponseDto;
import com.portfolio.exception.NotFoundException;
import com.portfolio.repository.BlogRepository;
import com.portfolio.repository.CommentRepository;
import com.portfolio.repository.LikesRepository;
import com.portfolio.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService {

    private final LikesRepository likesRepository;
    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;

    @Override
    public LikeResponseDto like(LikeTargetType targetType, Long targetId, String ipHash) {
        validateTarget(targetType, targetId);

        if (likesRepository.existsByTargetTypeAndTargetIdAndIpHash(targetType, targetId, ipHash)) {
            return new LikeResponseDto(getLikeCount(targetType, targetId), true);
        }

        likesRepository.saveAndFlush(new Likes(targetType, targetId, ipHash));
        increaseLikeCount(targetType, targetId);
        return new LikeResponseDto(getLikeCount(targetType, targetId), false);
    }

    private void validateTarget(LikeTargetType targetType, Long targetId) {
        boolean exists = switch (targetType) {
            case BLOG -> blogRepository.existsByIdAndIsPublishedTrueAndIsDeletedFalse(targetId);
            case COMMENT -> commentRepository.existsByIdAndIsDeletedFalse(targetId);
        };
        if (!exists) {
            throw new NotFoundException("좋아요 대상을 찾을 수 없습니다. " + targetType + " id=" + targetId);
        }
    }

    private void increaseLikeCount(LikeTargetType targetType, Long targetId) {
        switch (targetType) {
            case BLOG -> blogRepository.increaseLikeCount(targetId);
            case COMMENT -> commentRepository.increaseLikeCount(targetId);
        }
    }

    private int getLikeCount(LikeTargetType targetType, Long targetId) {
        return switch (targetType) {
            case BLOG -> blogRepository.findLikeCountById(targetId);
            case COMMENT -> commentRepository.findLikeCountById(targetId);
        };
    }
}
