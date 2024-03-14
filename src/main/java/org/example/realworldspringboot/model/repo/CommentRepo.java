package org.example.realworldspringboot.model.repo;

import org.example.realworldspringboot.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Integer> {
}
