package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.services.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikeApiController {
    final private LikeService likeService;

    @PostMapping("/like")
    public void like(@RequestParam UUID postId, Principal principal) {
        likeService.like(postId,principal.getName());
    }
    @PostMapping("/dislike")
    public void dislike(@RequestParam UUID postId, Principal principal) {
        likeService.dislike(postId,principal.getName());
    }
    @PostMapping("/remove")
    public void remove(@RequestParam UUID postId, Principal principal) {
        likeService.remove(postId,principal.getName());
    }
    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getStatus(@RequestParam UUID postId, Principal principal) {
        String status = likeService.getStatus(postId, principal.getName());
        return ResponseEntity.ok(Map.of("status", status));
    }


}
