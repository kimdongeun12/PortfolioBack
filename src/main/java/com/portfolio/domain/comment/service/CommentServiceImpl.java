package com.portfolio.domain.comment.service;

import com.portfolio.domain.blog.repository.BlogRepository;
import com.portfolio.domain.comment.dto.CommentCreateRequestDto;
import com.portfolio.domain.comment.dto.CommentResponseDto;
import com.portfolio.domain.comment.entity.Comment;
import com.portfolio.domain.comment.entity.CommentTargetType;
import com.portfolio.domain.comment.repository.CommentRepository;
import com.portfolio.domain.project.repository.ProjectRepository;
import com.portfolio.global.exception.BadRequestException;
import com.portfolio.global.exception.ForbiddenException;
import com.portfolio.global.exception.NotFoundException;

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

    private static final Long GUEST_TARGET_ID = 1L;

    @Value("${app.admin.nickname}")
    private String adminNickname;

    @Override
    public List<CommentResponseDto> findByTarget(CommentTargetType targetType, Long targetId) {
        validateTarget(targetType, targetId);

        List<Comment> comments = commentRepository.findByTargetTypeAndTargetIdOrderByCreatedAtAscIdAsc(targetType, targetId);

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

    private record Placement(CommentTargetType targetType, Long targetId, Long parentId) {
    }

    private Placement resolvePlacement(CommentTargetType targetType, Long targetId, Long parentId) {
        if (parentId != null) {
            Comment parent = commentRepository.findByIdAndIsDeletedFalse(parentId)
                    .orElseThrow(() -> new NotFoundException("답글을 달 댓글을 찾을 수 없습니다. id=" + parentId));
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

    private void validateTarget(CommentTargetType targetType, Long targetId) {
        boolean exists = switch (targetType) {
            case BLOG -> blogRepository.existsByIdAndIsPublishedTrueAndIsDeletedFalse(targetId);
            case PROJECT -> projectRepository.existsByIdAndIsDeletedFalse(targetId);
            case GUEST -> GUEST_TARGET_ID.equals(targetId);
        };
        if (!exists) {
            throw new NotFoundException("댓글 대상을 찾을 수 없습니다. " + targetType + " id=" + targetId);
        }
    }
}
