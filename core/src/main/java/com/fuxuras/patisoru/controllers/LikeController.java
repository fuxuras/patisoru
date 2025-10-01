package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.services.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/likes")
public class LikeController {
    final private LikeService likeService;

    @PostMapping("/like")
    public ResponseEntity<Long> like(@PathVariable UUID postId, @AuthenticationPrincipal UserDetails userDetails) {
        long likeCount = likeService.like(postId, userDetails.getUsername());
        return ResponseEntity.ok(likeCount);
    }

    @PostMapping("/dislike")
    public ResponseEntity<Long> dislike(@PathVariable UUID postId, @AuthenticationPrincipal UserDetails userDetails) {
        long likeCount = likeService.dislike(postId, userDetails.getUsername());
        return ResponseEntity.ok(likeCount);
    }

    @PostMapping("/remove")
    public ResponseEntity<Long> remove(@PathVariable UUID postId, @AuthenticationPrincipal UserDetails userDetails) {
        long likeCount = likeService.remove(postId, userDetails.getUsername());
        return ResponseEntity.ok(likeCount);
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus(@PathVariable UUID postId, @AuthenticationPrincipal UserDetails userDetails) {
        String status = likeService.getStatus(postId, userDetails.getUsername());
        return ResponseEntity.ok(status);
    }
}
