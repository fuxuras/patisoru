package com.fuxuras.patisoru.repositories;

import com.fuxuras.patisoru.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
