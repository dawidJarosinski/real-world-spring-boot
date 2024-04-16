package org.example.realworldspringboot.repo;

import org.example.realworldspringboot.model.entity.Article;
import org.example.realworldspringboot.model.entity.ArticleFavorited;
import org.example.realworldspringboot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleFavoritedRepo extends JpaRepository<ArticleFavorited, Integer> {

    Integer countArticleFavoritedByArticle(Article article);

    Boolean existsArticleFavoritedByArticleAndUser(Article article, User user);

    Optional<ArticleFavorited> findArticleFavoritedByArticleAndUser(Article article, User user);
}
