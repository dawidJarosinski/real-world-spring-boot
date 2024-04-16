package org.example.realworldspringboot.repo;

import org.example.realworldspringboot.model.entity.Article;
import org.example.realworldspringboot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepo extends JpaRepository<Article, Integer> {

    List<Article> findArticlesByAuthor(User author);

    Optional<Article> findArticleBySlug(String slug);

    Boolean existsArticleByTitle(String slug);
}
