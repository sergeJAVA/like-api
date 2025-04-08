package com.example.like_api.repository;

import com.example.like_api.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    void deleteByPostIdAndUserId(Long postId, Long userId);

    Long countByPostId(Long postId);

    List<Like> findByPostId(Long postId);

    boolean existsByPostIdAndUserId(Long postId, Long userId);

    List<Like> findByUserId(Long userId);

    void deleteByPostId(Long postId);

}
