package com.portfolio.domain.image.repository;

import com.portfolio.domain.image.entity.Image;
import com.portfolio.domain.image.entity.ImageTargetType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByTargetTypeAndTargetIdAndIsDeletedFalseOrderByIdAsc(ImageTargetType targetType, Long targetId);

    Optional<Image> findByIdAndIsDeletedFalse(Long id);
}
