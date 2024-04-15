package org.example.realworldspringboot.repo;

import org.example.realworldspringboot.model.entity.Article;
import org.example.realworldspringboot.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

    List<Comment> findCommentsByArticle(Article article);

    Optional<Comment> findCommentById(Integer id);
}
