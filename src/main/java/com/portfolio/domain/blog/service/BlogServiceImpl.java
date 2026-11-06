package com.portfolio.domain.blog.service;

import com.portfolio.domain.blog.dto.BlogRequestDto;
import com.portfolio.domain.blog.dto.BlogResponseDto;
import com.portfolio.domain.blog.dto.BlogSummaryDto;
import com.portfolio.domain.blog.entity.Blog;
import com.portfolio.domain.blog.repository.BlogRepository;
import com.portfolio.domain.comment.entity.CommentTargetType;
import com.portfolio.domain.comment.repository.CommentRepository;
import com.portfolio.domain.like.entity.LikeTargetType;
import com.portfolio.domain.like.repository.LikesRepository;
import com.portfolio.domain.techstack.service.TechStackService;
import com.portfolio.global.common.PageResponseDto;
import com.portfolio.global.exception.NotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final TechStackService techStackService;

    @Override
    public PageResponseDto<BlogSummaryDto> findPublished(String tech, Pageable pageable) {
        Page<Blog> page = (tech == null || tech.isBlank())
                ? blogRepository.findByIsPublishedTrueAndIsDeletedFalse(pageable)
                : blogRepository.findDistinctByIsPublishedTrueAndIsDeletedFalseAndTechStacks_Name(tech.trim(), pageable);
        Map<Long, Long> commentCounts = countComments(page.getContent());
        return new PageResponseDto<>(page, blog -> new BlogSummaryDto(blog, commentCounts.getOrDefault(blog.getId(), 0L)));
    }

    @Override
    public BlogResponseDto findPublishedById(Long id, String ipHash) {
        Blog blog = blogRepository.findByIdAndIsPublishedTrueAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("블로그 글을 찾을 수 없습니다. id=" + id));
        boolean liked = likesRepository.existsByTargetTypeAndTargetIdAndIpHash(LikeTargetType.BLOG, id, ipHash);
        return new BlogResponseDto(blog, liked);
    }

    @Override
    public PageResponseDto<BlogSummaryDto> findAllForAdmin(Pageable pageable) {
        return new PageResponseDto<>(blogRepository.findByIsDeletedFalse(pageable), BlogSummaryDto::new);
    }

    @Override
    public BlogResponseDto findByIdForAdmin(Long id) {
        return new BlogResponseDto(getBlog(id), false);
    }

    @Override
    @Transactional
    public Long create(BlogRequestDto requestDto) {
        Blog blog = requestDto.toEntity();
        blog.changeTechStacks(techStackService.resolve(requestDto.getTechStacks()));
        return blogRepository.save(blog).getId();
    }

    @Override
    @Transactional
    public BlogResponseDto update(Long id, BlogRequestDto requestDto) {
        Blog blog = getBlog(id);
        blog.update(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getThumbnailUrl(),
                requestDto.isPublished()
        );
        blog.changeTechStacks(techStackService.resolve(requestDto.getTechStacks()));
        return new BlogResponseDto(blog, false);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        getBlog(id).delete();
    }

    private Map<Long, Long> countComments(List<Blog> blogs) {
        if (blogs.isEmpty()) {
            return Map.of();
        }
        List<Long> ids = blogs.stream().map(Blog::getId).toList();
        Map<Long, Long> counts = new HashMap<>();
        for (Object[] row : commentRepository.countByTargets(CommentTargetType.BLOG, ids)) {
            counts.put((Long) row[0], (Long) row[1]);
        }
        return counts;
    }

    private Blog getBlog(Long id) {
        return blogRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("블로그 글을 찾을 수 없습니다. id=" + id));
    }
}
