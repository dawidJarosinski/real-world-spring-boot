package org.example.realworldspringboot.model.repo;

import org.example.realworldspringboot.model.entity.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTagRepo extends JpaRepository<ArticleTag, Integer> {
}
