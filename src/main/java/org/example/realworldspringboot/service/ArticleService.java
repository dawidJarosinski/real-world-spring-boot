package org.example.realworldspringboot.service;

import lombok.RequiredArgsConstructor;
import org.example.realworldspringboot.config.exceptions.ArticleNotFoundException;
import org.example.realworldspringboot.config.exceptions.ArticleWithThisTitleAlreadyExistsException;
import org.example.realworldspringboot.config.exceptions.CantManageOtherUsersArticlesException;
import org.example.realworldspringboot.dto.request.ArticleRequest;
import org.example.realworldspringboot.dto.request.UpdateArticleRequest;
import org.example.realworldspringboot.dto.response.ArticleResponse;
import org.example.realworldspringboot.dto.response.ProfileResponse;
import org.example.realworldspringboot.model.entity.Article;
import org.example.realworldspringboot.model.entity.ArticleTag;
import org.example.realworldspringboot.model.entity.Tag;
import org.example.realworldspringboot.model.entity.User;
import org.example.realworldspringboot.repo.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepo articleRepo;
    private final TagRepo tagRepo;
    private final UserRepo userRepo;
    private final ArticleTagRepo articleTagRepo;
    private final ArticleFavoritedRepo articleFavoritedRepo;
    private final UserFollowRepo userFollowRepo;

    public List<Article> findAllArticles(Map<String, String> params) {
        List<Article> articles = articleRepo.findAll();
        if(params.containsKey("author")) {
            articles = articles.stream().filter(article -> article.getAuthor().getUsername().equals(params.get("author"))).toList();
        }
//        if(params.containsKey("tag")) {
//            Tag tag = tagRepo.findTagByValue(params.get("tag"));
//            articles = articles.stream().filter(article -> article.getTags().contains(tag)).toList();
//        }
//        if(params.containsKey("favorited")) {
//            User user = userRepo.findUserByUsername(params.get("favorited")).get();
//            articles = articles.stream().filter(article -> article.getFavorited().contains(user)).toList();
//        }
        return articles;
    }

    public List<ArticleResponse> findAllArticles(String currentUserUsername) {
        User currentUser = userRepo.findUserByUsername(currentUserUsername).get();
        return articleRepo.findAll().stream().map(article -> buildArticleResponse(currentUser, article)).toList();
    }

    @Transactional
    public ArticleResponse createArticle(ArticleRequest request, String currentUserUsername) {
        User currentUser = userRepo.findUserByUsername(currentUserUsername).get();
        Article article = new Article(
                request.article().title(),
                request.article().description(),
                request.article().body(),
                currentUser);

        try{
            articleRepo.save(article);
        }
        catch (DataIntegrityViolationException ex) {
            throw new ArticleWithThisTitleAlreadyExistsException();
        }

        for(String tagStringValue : request.article().tagList()) {
            Tag tag;
            if(!tagRepo.existsTagByValue(tagStringValue)) {
                tag = new Tag(tagStringValue);
                tagRepo.save(tag);
            }
            else {
                tag = tagRepo.findTagByValue(tagStringValue);
            }
            articleTagRepo.save(new ArticleTag(article, tag));
        }

        return buildArticleResponse(currentUser, article);
    }

    @Transactional
    public ArticleResponse updateArticle(UpdateArticleRequest request,String slug, String currentUserUsername) {
        User currentUser = userRepo.findUserByUsername(currentUserUsername).get();
        Article articleToUpdate = articleRepo.findArticleBySlug(slug).orElseThrow(ArticleNotFoundException::new);

        if(articleToUpdate.getAuthor()!=currentUser) {
            throw new CantManageOtherUsersArticlesException();
        }

        if(request.article().body()!=null) {
            articleToUpdate.setBody(request.article().body());
        }
        if(request.article().description()!=null) {
            articleToUpdate.setDescription(request.article().description());
        }
        if(request.article().title()!=null && !articleRepo.existsArticleByTitle(request.article().title())) {
            articleToUpdate.setTitle(request.article().title());
            articleToUpdate.setSlug(Article.titleToSlug(request.article().title()));
        }
        else {
            throw new ArticleWithThisTitleAlreadyExistsException();
        }

        articleToUpdate.setUpdatedAt(LocalDateTime.now());
        articleRepo.save(articleToUpdate);

        return buildArticleResponse(currentUser, articleToUpdate);
    }

    @Transactional
    public void deleteArticle(String slug, String currentUserUsername) {
        User currentUser = userRepo.findUserByUsername(currentUserUsername).get();
        Article articleToDelete = articleRepo.findArticleBySlug(slug).orElseThrow(ArticleNotFoundException::new);

        if(!articleToDelete.getAuthor().equals(currentUser)) {
            throw new CantManageOtherUsersArticlesException();
        }

        List<ArticleTag> articleTagList = articleTagRepo.findArticleTagsByArticle(articleToDelete);
        for(ArticleTag articleTag : articleTagList) {
            if(articleTagRepo.countArticleTagByTag(articleTag.getTag()) <= 1) {
                tagRepo.delete(articleTag.getTag());
            }
            articleTagRepo.delete(articleTag);
        }
        articleRepo.delete(articleToDelete);
    }

    private ArticleResponse buildArticleResponse(User currentUser, Article article) {
        return ArticleResponse.builder()
                .article(
                        ArticleResponse.Article.builder()
                                .slug(article.getSlug())
                                .title(article.getTitle())
                                .description(article.getDescription())
                                .body(article.getBody())
                                .tagList(articleTagRepo.findArticleTagsByArticle(article).stream().map(articleTag -> articleTag.getTag().getValue()).toList())
                                .createdAt(article.getCreatedAt())
                                .updatedAt(article.getUpdatedAt())
                                .favorited(articleFavoritedRepo.existsArticleFavoritedByArticleAndUser(article, currentUser))
                                .favoritesCount(articleFavoritedRepo.countArticleFavoritedByArticle(article))
                                .author(ProfileResponse.Profile.builder()
                                        .username(article.getAuthor().getUsername())
                                        .bio(article.getAuthor().getBio())
                                        .image(article.getAuthor().getImage())
                                        .following(userFollowRepo.existsByFollowerAndFollowed(currentUser, article.getAuthor()))
                                        .build())
                                .build()
                )
                .build();
    }
}
