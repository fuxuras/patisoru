package com.fuxuras.patisoru.services;

import com.fuxuras.patisoru.configuration.DtoMapper;
import com.fuxuras.patisoru.dto.post.PostCreateRequest;
import com.fuxuras.patisoru.dto.post.PostResponse;
import com.fuxuras.patisoru.entities.Post;
import com.fuxuras.patisoru.entities.PostType;
import com.fuxuras.patisoru.entities.User;
import com.fuxuras.patisoru.exceptions.PostNotFoundException;
import com.fuxuras.patisoru.repositories.PostRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final DtoMapper mapper;
    private final UserService userService;
    private final LikeService likeService;

    public PostService(PostRepository postRepository, DtoMapper mapper, UserService userService, @Lazy LikeService likeService) {
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.userService = userService;
        this.likeService = likeService;
    }

    public PostResponse getPostById(UUID id, UserDetails userDetails) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));

        PostResponse postResponse = mapper.postToPostResponse(post);

        String userLikeStatus = "remove";
        if (userDetails != null) {
            userLikeStatus = likeService.getStatus(post.getId(), userDetails.getUsername());
        }
        postResponse.setUserLikeStatus(userLikeStatus);
        return postResponse;
    }

    @Cacheable(value = "featured_post")
    public List<PostResponse> getFeaturedPosts() {
        List<Post> posts = postRepository
                .findTop5ByCreatedAtAfterOrderByLikeCountDesc(LocalDateTime.now().minusMonths(1));
        return posts.stream()
                .map(mapper::postToPostResponse)
                .toList();
    }

    public Page<PostResponse> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(mapper::postToPostResponse);
    }

    public PostResponse create(PostCreateRequest postCreateRequest, String username) {
        User user = userService.findByEmail(username);
        Post post = mapper.PostCreateRequestToPost(postCreateRequest);
        post.setUser(user);

        if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_VET"))) {
            post.setPostType(PostType.VET_POST);
        } else if (user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_VERIFIED"))) {
            post.setPostType(PostType.VERIFIED_POST);
        } else {
            post.setPostType(PostType.USER_POST);
        }

        Post savedPost = postRepository.save(post);
        return mapper.postToPostResponse(savedPost);
    }

    @PreAuthorize("@postSecurityService.isOwner(authentication,#postId)")
    public void deletePost(UUID postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        postRepository.deleteById(postId);
    }

    protected Optional<Post> findPostById(UUID id) {
        return postRepository.findById(id);
    }

    protected void save(Post post) {
        postRepository.save(post);
    }
}