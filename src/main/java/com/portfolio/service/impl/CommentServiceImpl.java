package com.portfolio.service.impl;

import com.portfolio.domain.Comment;
import com.portfolio.domain.CommentTargetType;
import com.portfolio.dto.CommentCreateRequestDto;
import com.portfolio.dto.CommentResponseDto;
import com.portfolio.exception.BadRequestException;
import com.portfolio.exception.ForbiddenException;
import com.portfolio.exception.NotFoundException;
import com.portfolio.repository.BlogRepository;
import com.portfolio.repository.CommentRepository;
import com.portfolio.repository.ProjectRepository;
import com.portfolio.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;

    /** 관리자 댓글에 표시할 닉네임 */
    @Value("${app.admin.nickname}")
    private String adminNickname;

    @Override
    public List<CommentResponseDto> findByTarget(CommentTargetType targetType, Long targetId) {
        validateTarget(targetType, targetId);

        List<Comment> comments = commentRepository.findByTargetTypeAndTargetIdOrderByCreatedAtAscIdAsc(targetType, targetId);

        // 부모 id 별 대댓글 묶기 (숨김 처리된 대댓글은 제외)
        Map<Long, List<CommentResponseDto>> repliesByParent = new LinkedHashMap<>();
        for (Comment comment : comments) {
            if (comment.isReply() && !comment.isDeleted()) {
                repliesByParent.computeIfAbsent(comment.getParentId(), key -> new ArrayList<>())
                        .add(new CommentResponseDto(comment));
            }
        }

        List<CommentResponseDto> result = new ArrayList<>();
        for (Comment comment : comments) {
            if (comment.isReply()) {
                continue;
            }
            List<CommentResponseDto> replies = repliesByParent.getOrDefault(comment.getId(), List.of());
            // 삭제된 댓글은 대댓글이 남아있을 때만 "삭제된 댓글입니다." 로 표시하고, 없으면 아예 숨김
            if (comment.isDeleted() && replies.isEmpty()) {
                continue;
            }
            result.add(new CommentResponseDto(comment, replies));
        }
        return result;
    }

    @Override
    @Transactional
    public Long create(CommentCreateRequestDto requestDto) {
        Placement placement = resolvePlacement(requestDto.getTargetType(), requestDto.getTargetId(), requestDto.getParentId());

        Comment comment = Comment.builder()
                .targetType(placement.targetType())
                .targetId(placement.targetId())
                .parentId(placement.parentId())
                .nickname(requestDto.getNickname().trim())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .content(requestDto.getContent())
                .isAdmin(false)
                .build();
        return commentRepository.save(comment).getId();
    }

    @Override
    @Transactional
    public Long createByAdmin(CommentTargetType targetType, Long targetId, Long parentId, String content) {
        Placement placement = resolvePlacement(targetType, targetId, parentId);

        Comment comment = Comment.builder()
                .targetType(placement.targetType())
                .targetId(placement.targetId())
                .parentId(placement.parentId())
                .nickname(adminNickname)
                // 비밀번호 방식으로는 수정·삭제할 수 없도록 아무도 모르는 값의 해시를 저장
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .content(content)
                .isAdmin(true)
                .build();
        return commentRepository.save(comment).getId();
    }

    @Override
    @Transactional
    public CommentResponseDto updateByAdmin(Long id, String content) {
        Comment comment = commentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다. id=" + id));
        if (!comment.isAdmin()) {
            throw new ForbiddenException("관리자가 작성한 댓글만 수정할 수 있습니다.");
        }
        comment.updateContent(content);
        return new CommentResponseDto(comment);
    }

    /** 댓글이 달릴 위치 */
    private record Placement(CommentTargetType targetType, Long targetId, Long parentId) {
    }

    /**
     * - 대댓글(parentId 있음): 대상은 부모 댓글에서 복사, 깊이는 1단계로 제한
     * - 최상위 댓글: targetType, targetId 필수
     */
    private Placement resolvePlacement(CommentTargetType targetType, Long targetId, Long parentId) {
        if (parentId != null) {
            Comment parent = commentRepository.findByIdAndIsDeletedFalse(parentId)
                    .orElseThrow(() -> new NotFoundException("답글을 달 댓글을 찾을 수 없습니다. id=" + parentId));
            // 대댓글에 답글을 달면 같은 최상위 댓글 아래에 붙입니다.
            Long rootId = parent.isReply() ? parent.getParentId() : parent.getId();
            validateTarget(parent.getTargetType(), parent.getTargetId());
            return new Placement(parent.getTargetType(), parent.getTargetId(), rootId);
        }
        if (targetType == null || targetId == null) {
            throw new BadRequestException("댓글 대상(targetType, targetId)은 필수입니다.");
        }
        validateTarget(targetType, targetId);
        return new Placement(targetType, targetId, null);
    }

    @Override
    @Transactional
    public CommentResponseDto update(Long id, String password, String content) {
        Comment comment = getCommentWithPassword(id, password);
        comment.updateContent(content);
        return new CommentResponseDto(comment);
    }

    @Override
    @Transactional
    public void delete(Long id, String password) {
        getCommentWithPassword(id, password).delete();
    }

    @Override
    @Transactional
    public void deleteByAdmin(Long id) {
        Comment comment = commentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다. id=" + id));
        comment.delete();
    }

    private Comment getCommentWithPassword(Long id, String password) {
        Comment comment = commentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("댓글을 찾을 수 없습니다. id=" + id));
        if (!passwordEncoder.matches(password, comment.getPassword())) {
            throw new ForbiddenException("비밀번호가 일치하지 않습니다.");
        }
        return comment;
    }

    /** 댓글 대상(블로그/프로젝트)이 존재하고 숨김 처리되지 않았는지 확인 */
    private void validateTarget(CommentTargetType targetType, Long targetId) {
        boolean exists = switch (targetType) {
            case BLOG -> blogRepository.existsByIdAndIsPublishedTrueAndIsDeletedFalse(targetId);
            case PROJECT -> projectRepository.existsByIdAndIsDeletedFalse(targetId);
        };
        if (!exists) {
            throw new NotFoundException("댓글 대상을 찾을 수 없습니다. " + targetType + " id=" + targetId);
        }
    }
}
