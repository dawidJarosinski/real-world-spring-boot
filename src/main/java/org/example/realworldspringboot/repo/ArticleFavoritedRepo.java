package org.example.realworldspringboot.repo;

import org.example.realworldspringboot.model.entity.ArticleFavorited;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleFavoritedRepo extends JpaRepository<ArticleFavorited, Integer> {
}
