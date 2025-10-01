package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.CommentCreateRequest;
import com.fuxuras.patisoru.dto.comment.CommentResponse;
import com.fuxuras.patisoru.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentCreateRequest commentCreateRequest,
                                                 @AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable(name = "postId") UUID postId) {

        CommentResponse createdComment = commentService.create(commentCreateRequest, userDetails.getUsername(), postId);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable(name = "postId") UUID postId) {
        List<CommentResponse> comments = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

}
