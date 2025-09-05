package com.fuxuras.patisoru.services;

import com.fuxuras.patisoru.configuration.DtoMapper;
import com.fuxuras.patisoru.dto.FeaturedPost;
import com.fuxuras.patisoru.dto.PostCreateRequest;
import com.fuxuras.patisoru.dto.PostResponse;
import com.fuxuras.patisoru.entities.Post;
import com.fuxuras.patisoru.entities.User;
import com.fuxuras.patisoru.exceptions.PostNotFoundException;
import com.fuxuras.patisoru.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Post post = postRepository.findById(id).orElseThrow(()-> new PostNotFoundException("Post not found with id: " + id));
        return mapper.postToPostResponse(post);
    }

    @Cacheable(value = "featured_post")
    public List<FeaturedPost> getFeaturedPosts(){
        List<Post> posts = postRepository.findTop5ByCreatedAtAfterOrderByLikeCountDesc(LocalDateTime.now().minusMonths(1));
        List<FeaturedPost> featuredPosts = posts.stream()
                .map(mapper::postToFeaturedPost)
                .toList();
        return featuredPosts;
    }
    
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(mapper::postToPostResponse);
    }

    public PostResponse create(PostCreateRequest postCreateRequest, String username) {
        User user = userService.findByEmail(username);
        Post post = mapper.PostCreateRequestToPost(postCreateRequest);
        post.setUser(user);
        Post savedPost = postRepository.save(post);
        return mapper.postToPostResponse(savedPost);
    }

    protected Optional<Post> findPostById(UUID id) {
        return postRepository.findById(id);
    }

    protected void save(Post post) {
        postRepository.save(post);
    }
}
