package com.fuxuras.patisoru.services;

import com.fuxuras.patisoru.configuration.DtoMapper;
import com.fuxuras.patisoru.dto.post.PostCreateRequest;
import com.fuxuras.patisoru.dto.post.PostResponse;
import com.fuxuras.patisoru.entities.Post;
import com.fuxuras.patisoru.entities.PostType;
import com.fuxuras.patisoru.entities.User;
import com.fuxuras.patisoru.exceptions.PostNotFoundException;
import com.fuxuras.patisoru.exceptions.UnauthorizedPostAccessException;
import com.fuxuras.patisoru.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final DtoMapper mapper;
    private final UserService userService;


    public PostResponse getPostById(UUID id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + id));
        return mapper.postToPostResponse(post);
    }

    @Cacheable(value = "featured_post")
    public List<PostResponse> getFeaturedPosts() {
        List<Post> posts = postRepository
                .findTop5ByCreatedAtAfterOrderByLikeCountDesc(LocalDateTime.now().minusMonths(1));
        List<PostResponse> postResponse = posts.stream()
                .map(mapper::postToPostResponse)
                .toList();
        return postResponse;
    }

    public Page<PostResponse> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(mapper::postToPostResponse);
    }

    public PostResponse create(PostCreateRequest postCreateRequest, String username) {
        User user = userService.findByEmail(username);
        Post post = mapper.PostCreateRequestToPost(postCreateRequest);
        post.setUser(user);

        // Automatically determine the PostType based on user roles
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

    @PreAuthorize("@postRepository.findById(#postId).get().getUser().getEmail() == authentication.name")
    public void deletePost(UUID postId){
        Post post = postRepository.findById(postId)
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
