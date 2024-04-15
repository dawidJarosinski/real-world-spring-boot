package org.example.realworldspringboot.repo;

import org.example.realworldspringboot.model.entity.Article;
import org.example.realworldspringboot.model.entity.ArticleTag;
import org.example.realworldspringboot.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleTagRepo extends JpaRepository<ArticleTag, Integer> {

    List<ArticleTag> findArticleTagsByArticle(Article article);

    Integer countArticleTagByTag(Tag tag);
}
