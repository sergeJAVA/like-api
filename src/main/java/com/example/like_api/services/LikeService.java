package com.example.like_api.services;


import com.example.like_api.model.Like;

import java.util.List;

public interface LikeService {
    void addLike(Long postId, Long userId);
    void removeLike(Long postId, Long userId);

    Long getLikesCount(Long postId);
    List<Long> getLikedUsers(Long postId);
    boolean isLiked(Long postId, Long userId);

    List<Long> getLikedPosts(Long userId);

    List<Long> getMutualLikedPosts(Long userId1, Long userId2);

    List<Like> getAllLikes();
}
