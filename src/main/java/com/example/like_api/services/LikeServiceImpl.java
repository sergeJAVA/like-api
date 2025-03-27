package com.example.like_api.services;

import com.example.like_api.model.Like;
import com.example.like_api.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{

    private final LikeRepository likeRepository;

    @Override
    public void addLike(Long postId, Long userId) {
        if (!isLiked(postId, userId)) {
            Like like = Like.builder()
                    .postId(postId)
                    .userId(userId)
                    .timestamp(LocalDateTime.now())
                    .build();
            likeRepository.save(like);
        }
    }

    @Override
    @Transactional
    public void removeLike(Long postId, Long userId) {
        likeRepository.deleteByPostIdAndUserId(postId, userId);
    }

    @Override
    public Long getLikesCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    @Override
    public List<Long> getLikedUsers(Long postId) {
        return likeRepository.findByPostId(postId).stream()
                .map(Like::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isLiked(Long postId, Long userId) {
        return likeRepository.existsByPostIdAndUserId(postId, userId);
    }

    @Override
    public List<Long> getLikedPosts(Long userId) {
        return likeRepository.findByUserId(userId).stream()
                .map(Like::getPostId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getMutualLikedPosts(Long userId1, Long userId2) {
        List<Long> likedPosts1 = getLikedPosts(userId1);
        List<Long> likedPosts2 = getLikedPosts(userId2);
        return likedPosts1.stream()
                .filter(likedPosts2::contains)
                .collect(Collectors.toList());
    }

    @Override
    public List<Like> getAllLikes() {
        return likeRepository.findAll();
    }
}
