package com.portfolio.service;

import com.portfolio.domain.CommentTargetType;
import com.portfolio.dto.CommentCreateRequestDto;
import com.portfolio.dto.CommentResponseDto;

import java.util.List;

public interface CommentService {

    List<CommentResponseDto> findByTarget(CommentTargetType targetType, Long targetId);

    Long create(CommentCreateRequestDto requestDto);

    CommentResponseDto update(Long id, String password, String content);

    void delete(Long id, String password);

    void deleteByAdmin(Long id);

    Long createByAdmin(CommentTargetType targetType, Long targetId, Long parentId, String content);

    CommentResponseDto updateByAdmin(Long id, String content);
}
