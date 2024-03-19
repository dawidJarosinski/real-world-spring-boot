package org.example.realworldspringboot.repo;

import org.example.realworldspringboot.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepo extends JpaRepository<Article, Integer> {
}
