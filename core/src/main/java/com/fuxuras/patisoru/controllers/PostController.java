package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.PostCreateRequest;
import com.fuxuras.patisoru.dto.PostResponse;
import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.services.LikeService;
import com.fuxuras.patisoru.services.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final LikeService likeService;

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        PostResponse post = postService.getPostById(id);
        String userLikeStatus = "remove"; // Default for non-logged-in users
        if (userDetails != null) {
            userLikeStatus = likeService.getStatus(post.getId(), userDetails.getUsername());
        }
        post.setUserLikeStatus(userLikeStatus);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> create(@Valid @RequestBody PostCreateRequest post, @AuthenticationPrincipal UserDetails userDetails) {
        ResponseMessage message = postService.create(post, userDetails.getUsername());
        // For REST APIs, it's better to return a specific HTTP status code
        // 201 Created is the standard for a successful POST request.
        if (message.getCode() > 0) {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    // TODO: feature: post update
    // TODO: feature: post delete
}
