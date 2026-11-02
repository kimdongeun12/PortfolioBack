package com.portfolio.service;

import com.portfolio.domain.CommentTargetType;
import com.portfolio.dto.CommentCreateRequestDto;
import com.portfolio.dto.CommentResponseDto;

import java.util.List;

/**
 * 댓글 비즈니스 로직 인터페이스
 */
public interface CommentService {

    /** 대상의 댓글 목록 (최상위 댓글 + 대댓글 중첩) */
    List<CommentResponseDto> findByTarget(CommentTargetType targetType, Long targetId);

    /** 작성 (생성된 id 반환) */
    Long create(CommentCreateRequestDto requestDto);

    /** 수정 (비밀번호 확인) */
    CommentResponseDto update(Long id, String password, String content);

    /** 삭제 — 숨김 처리 (비밀번호 확인) */
    void delete(Long id, String password);

    /** [관리자] 비밀번호 없이 숨김 처리 */
    void deleteByAdmin(Long id);

    /** [관리자] 관리자 댓글 작성 (비밀번호 없음, 닉네임은 설정값) */
    Long createByAdmin(CommentTargetType targetType, Long targetId, Long parentId, String content);

    /** [관리자] 관리자 댓글 수정 (관리자가 쓴 댓글만) */
    CommentResponseDto updateByAdmin(Long id, String content);
}
