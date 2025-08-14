package com.fuxuras.patisoru.repositories;

import com.fuxuras.patisoru.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findTop5ByCreatedAtAfterOrderByLikeCountDesc(LocalDateTime dateTime);
}
