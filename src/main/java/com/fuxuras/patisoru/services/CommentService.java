package com.fuxuras.patisoru.services;

import com.fuxuras.patisoru.configuration.DtoMapper;
import com.fuxuras.patisoru.dto.CommentCreateRequest;
import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.entities.Comment;
import com.fuxuras.patisoru.entities.Post;
import com.fuxuras.patisoru.entities.User;
import com.fuxuras.patisoru.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final DtoMapper mapper;
    private final PostService postService;

    public ResponseMessage create(CommentCreateRequest commentCreateRequest, String email, UUID postId) {
        Comment comment = mapper.CommentCreateRequestToComment(commentCreateRequest);
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postService.findPostById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        comment.setUser(user);
        comment.setPost(post);
        commentRepository.save(comment);
        return new ResponseMessage("Yorum gönderildi.",1);
    }
}
