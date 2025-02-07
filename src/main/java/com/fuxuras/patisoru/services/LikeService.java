package com.fuxuras.patisoru.services;

import com.fuxuras.patisoru.entities.Like;
import com.fuxuras.patisoru.entities.LikeId;
import com.fuxuras.patisoru.entities.Post;
import com.fuxuras.patisoru.entities.User;
import com.fuxuras.patisoru.repositories.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final UserService userService;
    private final LikeRepository likeRepository;
    private final PostService postService;

    /*
     * These like dislike and remove functions are completely bullshit
     * TODO: refactor: like, dislike, remove and saveLike
     */
    public void like(UUID postId, String email) {
        saveLike(postId,email,true);
    }

    public void dislike(UUID postId, String email) {
        saveLike(postId,email,false);
    }

    public void remove(UUID postId, String email) {
        saveLike(postId,email,null);
    }

    private void saveLike(UUID postId,String email, Boolean isLike) {
        User user = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Post post = postService.findPostById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        LikeId likeId = new LikeId(postId,user.getId());
        if (isLike == null) {
            likeRepository.deleteById(likeId);
        }else{
            Like like = new Like();
            like.setId(likeId);
            like.setIsLike(isLike);
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
        }
        updateLikeCount(postId);
    }

    private void updateLikeCount(UUID postId) {
        Post post = postService.findPostById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        long likes = likeRepository.countLikesByPostId(postId);
        long dislikes = likeRepository.countDislikesByPostId(postId);
        post.setLikeCount(likes-dislikes);
        postService.save(post);
    }

    public String getStatus(UUID postId, String name) {
        User user = userService.findByEmail(name).orElseThrow(() -> new RuntimeException("User not found"));
        LikeId likeId = new LikeId(postId,user.getId());
        return likeRepository.findById(likeId)
                .map(like -> {
                    Boolean isLike = like.getIsLike();
                    if (isLike == null) {
                        return "remove"; // Default value when isLike is null
                    }
                    return isLike ? "like" : "dislike";
                })
                .orElse("remove");
    }
}
