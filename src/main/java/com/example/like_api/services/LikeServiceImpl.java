package com.example.like_api.services;

import com.example.like_api.model.Like;
import com.example.like_api.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @CacheEvict(value = "likesPost", key = "#postId")
    public ResponseEntity<String> addLike(Long postId, Long userId) {
        if (!isLiked(postId, userId)) {
            Like like = Like.builder()
                    .postId(postId)
                    .userId(userId)
                    .timestamp(LocalDateTime.now())
                    .build();
            likeRepository.save(like);
            return ResponseEntity.ok("LIKE successfully added!");
        }
        return new ResponseEntity<>("User has already liked this post", HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional
    @CacheEvict(value = "likesPost", key = "#postId")
    public ResponseEntity<String> removeLike(Long postId, Long userId) {
        if (isLiked(postId, userId)) {
            likeRepository.deleteByPostIdAndUserId(postId, userId);
            return ResponseEntity.ok("Like successfully deleted!");
        }
        return new ResponseEntity<>("User hasn't liked this post yet", HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional
    @CacheEvict(value = "likesPost", allEntries = true)
    public ResponseEntity<String> removeAllLikesFromPost(Long postId) {
        likeRepository.deleteByPostId(postId);
        return ResponseEntity.ok("All likes from post id \"" + postId + "\" have been removed");
    }

    @Override
    @Cacheable(value = "likesPost", key = "#postId")
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
