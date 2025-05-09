package com.fuxuras.patisoru.services;

import com.fuxuras.patisoru.configuration.DtoMapper;
import com.fuxuras.patisoru.dto.FeaturedPost;
import com.fuxuras.patisoru.dto.PostCreateRequest;
import com.fuxuras.patisoru.dto.PostResponse;
import com.fuxuras.patisoru.dto.ResponseMessage;
import com.fuxuras.patisoru.entities.Post;
import com.fuxuras.patisoru.entities.User;
import com.fuxuras.patisoru.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final DtoMapper mapper;
    private final UserService userService;


    public PostResponse getPostById(UUID id) {
        Post post = postRepository.findById(id).orElseThrow(()-> new RuntimeException("Post not found"));
        PostResponse postResponse = mapper.postToPostResponse(post);
        return postResponse;
    }

    @Cacheable(value = "featured_post")
    public List<FeaturedPost> getFeaturedPosts(){
        List<Post> posts = postRepository.findTop5ByCreatedAtAfterOrderByLikeCountDesc(LocalDateTime.now().minusMonths(1));
        List<FeaturedPost> featuredPosts = posts.stream()
                .map(mapper::postToFeaturedPost)
                .collect(Collectors.toList());
        return featuredPosts;
    }
    
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(mapper::postToPostResponse);
    }

    public ResponseMessage create(PostCreateRequest postCreateRequest, String name) {
        User user = userService.findByEmail(name).orElseThrow(()-> new RuntimeException("User not found"));
        Post post = mapper.PostCreateRequestToPost(postCreateRequest);
        post.setUser(user);
        postRepository.save(post);

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Gönderiniz başarıyla paylaşıldı.");
        responseMessage.setCode(1);
        return responseMessage;
    }

    protected Optional<Post> findPostById(UUID id) {
        return postRepository.findById(id);
    }

    protected void save(Post post) {
        postRepository.save(post);
    }
}
