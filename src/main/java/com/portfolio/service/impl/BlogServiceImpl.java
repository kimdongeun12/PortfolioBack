package com.portfolio.service.impl;

import com.portfolio.domain.Blog;
import com.portfolio.domain.LikeTargetType;
import com.portfolio.dto.BlogRequestDto;
import com.portfolio.dto.BlogResponseDto;
import com.portfolio.dto.BlogSummaryDto;
import com.portfolio.dto.PageResponseDto;
import com.portfolio.exception.NotFoundException;
import com.portfolio.repository.BlogRepository;
import com.portfolio.repository.LikesRepository;
import com.portfolio.service.BlogService;
import com.portfolio.service.TechStackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final LikesRepository likesRepository;
    private final TechStackService techStackService;

    @Override
    public PageResponseDto<BlogSummaryDto> findPublished(String tech, Pageable pageable) {
        Page<Blog> page = (tech == null || tech.isBlank())
                ? blogRepository.findByIsPublishedTrueAndIsDeletedFalse(pageable)
                : blogRepository.findDistinctByIsPublishedTrueAndIsDeletedFalseAndTechStacks_Name(tech.trim(), pageable);
        return new PageResponseDto<>(page, BlogSummaryDto::new);
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

    private Blog getBlog(Long id) {
        return blogRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("블로그 글을 찾을 수 없습니다. id=" + id));
    }
}
