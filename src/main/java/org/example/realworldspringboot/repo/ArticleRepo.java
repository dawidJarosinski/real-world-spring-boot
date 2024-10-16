package org.example.realworldspringboot.repo;

import org.example.realworldspringboot.model.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepo extends JpaRepository<Article, Integer> {
    Optional<Article> findArticleBySlug(String slug);

    Boolean existsArticleByTitle(String title);
}
