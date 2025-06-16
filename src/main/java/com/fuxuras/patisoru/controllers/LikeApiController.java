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

    /*
     * TODO: refactor: these must return response entity
     * these all like dislike remove must return something
     * for informing user with ui
     */
    @PostMapping("/like")
    public long like(@RequestParam UUID postId, Principal principal) {
        return likeService.like(postId,principal.getName());
    }

    @PostMapping("/dislike")
    public long dislike(@RequestParam UUID postId, Principal principal) {
        return likeService.dislike(postId,principal.getName());
    }

    @PostMapping("/remove")
    public long remove(@RequestParam UUID postId, Principal principal) {
        return likeService.remove(postId,principal.getName());
    }

    // TODO: refactor: response entity shape
    // i dont think this is best practice usage of response entity
    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getStatus(@RequestParam UUID postId, Principal principal) {
        String status = likeService.getStatus(postId, principal.getName());
        return ResponseEntity.ok(Map.of("status", status));
    }


}
