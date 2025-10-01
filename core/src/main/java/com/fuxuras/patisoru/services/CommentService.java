package com.fuxuras.patisoru.services;

import com.fuxuras.patisoru.configuration.DtoMapper;
import com.fuxuras.patisoru.dto.CommentCreateRequest;
import com.fuxuras.patisoru.dto.CommentInPostResponse;
import com.fuxuras.patisoru.entities.Comment;
import com.fuxuras.patisoru.entities.Post;
import com.fuxuras.patisoru.entities.User;
import com.fuxuras.patisoru.exceptions.PostNotFoundException;
import com.fuxuras.patisoru.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final DtoMapper mapper;
    private final PostService postService;

    public CommentInPostResponse create(CommentCreateRequest commentCreateRequest, String email, UUID postId) {
        Comment comment = mapper.CommentCreateRequestToComment(commentCreateRequest);
        User user = userService.findByEmail(email);
        Post post = postService.findPostById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " not found"));
        comment.setUser(user);
        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);
        return mapper.commentToCommentInPostResponse(savedComment);
    }

    public List<CommentInPostResponse> getCommentsByPostId(UUID postId) {
        postService.findPostById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " not found"));
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(mapper::commentToCommentInPostResponse)
                .collect(Collectors.toList());
    }
}
