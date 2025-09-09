package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.post.PostCreateRequest;
import com.fuxuras.patisoru.dto.post.PostResponse;
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

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        PostResponse post = postService.getPostById(id, userDetails);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostCreateRequest post, @AuthenticationPrincipal UserDetails userDetails) {
        PostResponse createdPost = postService.create(post, userDetails.getUsername());
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
