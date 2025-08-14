package com.fuxuras.patisoru.controllers;

import com.fuxuras.patisoru.dto.CommentCreateRequest;
import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.services.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createComment(@Valid @RequestBody CommentCreateRequest commentCreateRequest,
                                                       @AuthenticationPrincipal UserDetails userDetails,
                                                       @PathVariable(name = "postId") UUID postId) {

        ResponseMessage message = commentService.create(commentCreateRequest, userDetails.getUsername(), postId);
        if (message.getCode() > 0) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }

    //TODO: feature: comment delete

    //TODO: feature: comment update

}
