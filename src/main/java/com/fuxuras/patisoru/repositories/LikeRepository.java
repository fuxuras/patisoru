package com.fuxuras.patisoru.repositories;

import com.fuxuras.patisoru.entities.Like;
import com.fuxuras.patisoru.entities.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, LikeId> {

    @Query("SELECT COUNT(l) FROM Like l WHERE l.id.postId = :postId AND l.isLike = true")
    long countLikesByPostId(@Param("postId") UUID postId);


    @Query("SELECT COUNT(l) FROM Like l WHERE l.id.postId = :postId AND l.isLike = false")
    long countDislikesByPostId(@Param("postId") UUID postId);

    @Query("SELECT SUM(CASE WHEN l.isLike = true THEN 1 ELSE -1 END) FROM Like l WHERE l.id.postId = :postId")
    long getLikeDislikeDifferenceByPostId(@Param("postId") UUID postId);
}
