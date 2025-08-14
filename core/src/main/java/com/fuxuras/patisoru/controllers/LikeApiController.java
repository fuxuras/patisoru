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
public class LikeApiController {
    final private LikeService likeService;

    @PostMapping("/like")
    public ResponseEntity<ResponseMessage> like(@PathVariable UUID postId, @AuthenticationPrincipal UserDetails userDetails) {
        long likeCount = likeService.like(postId, userDetails.getUsername());
        return ResponseEntity.ok(new ResponseMessage("Post liked", (int) likeCount));
    }

    @PostMapping("/dislike")
    public ResponseEntity<ResponseMessage> dislike(@PathVariable UUID postId, @AuthenticationPrincipal UserDetails userDetails) {
        long likeCount = likeService.dislike(postId, userDetails.getUsername());
        return ResponseEntity.ok(new ResponseMessage("Post disliked", (int) likeCount));
    }

    @PostMapping("/remove")
    public ResponseEntity<ResponseMessage> remove(@PathVariable UUID postId, @AuthenticationPrincipal UserDetails userDetails) {
        long likeCount = likeService.remove(postId, userDetails.getUsername());
        return ResponseEntity.ok(new ResponseMessage("Like removed", (int) likeCount));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getStatus(@PathVariable UUID postId, @AuthenticationPrincipal UserDetails userDetails) {
        String status = "remove";
        if (userDetails != null) {
            status = likeService.getStatus(postId, userDetails.getUsername());
        }
        return ResponseEntity.ok(Map.of("status", status));
    }
}
