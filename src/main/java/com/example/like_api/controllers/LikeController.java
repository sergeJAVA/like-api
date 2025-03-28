package com.example.like_api.controllers;

import com.example.like_api.model.Like;
import com.example.like_api.services.LikeService;
import com.example.like_api.services.security.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final JWTService jwtService;

    @GetMapping("/all")
    public List<Like> allLikes() {
        return likeService.getAllLikes();
    }

    @PostMapping("/{postId}")
    public ResponseEntity<String> addLike(@PathVariable Long postId, @CookieValue("token") String token) {
        Long userId = jwtService.getUserIdFromToken(token);
        return likeService.addLike(postId, userId);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> removeLike(@PathVariable Long postId, @CookieValue("token") String token) {
        Long userId = jwtService.getUserIdFromToken(token);
        return likeService.removeLike(postId, userId);
    }

    @GetMapping("/{postId}/count")
    public ResponseEntity<Long> getLikesCount(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.getLikesCount(postId));
    }

    @GetMapping("/{postId}/users")
    public ResponseEntity<List<Long>> getLikedUsers(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.getLikedUsers(postId));
    }

    @GetMapping("/{postId}/users/{userId}")
    public ResponseEntity<Boolean> isLiked(@PathVariable Long postId, @PathVariable Long userId) {
        return ResponseEntity.ok(likeService.isLiked(postId, userId));
    }

    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<List<Long>> getLikedPosts(@PathVariable Long userId) {
        return ResponseEntity.ok(likeService.getLikedPosts(userId));
    }

    @GetMapping("/mutual/{userId1}/{userId2}")
    public ResponseEntity<List<Long>> getMutualLikedPosts(@PathVariable Long userId1, @PathVariable Long userId2) {
        return ResponseEntity.ok(likeService.getMutualLikedPosts(userId1, userId2));
    }
}
