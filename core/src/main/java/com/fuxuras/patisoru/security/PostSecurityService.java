package com.fuxuras.patisoru.security;

import com.fuxuras.patisoru.entities.Post;
import com.fuxuras.patisoru.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostSecurityService {
    private final PostRepository postRepository;

    public boolean isOwner(Authentication authentication, UUID postId){
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isEmpty()){
            return false;
        }
        String postOwnerEmail = postOptional.get().getUser().getEmail();
        return postOwnerEmail.equals(authentication.getName());
    }
}
