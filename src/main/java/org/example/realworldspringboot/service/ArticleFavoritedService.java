package org.example.realworldspringboot.service;


import lombok.RequiredArgsConstructor;
import org.example.realworldspringboot.config.exceptions.AlreadyFavoritedException;
import org.example.realworldspringboot.config.exceptions.ArticleFavoritedNotFoundException;
import org.example.realworldspringboot.config.exceptions.ArticleNotFoundException;
import org.example.realworldspringboot.config.exceptions.UserNotFoundException;
import org.example.realworldspringboot.dto.response.ArticleResponse;
import org.example.realworldspringboot.dto.response.ProfileResponse;
import org.example.realworldspringboot.model.entity.Article;
import org.example.realworldspringboot.model.entity.ArticleFavorited;
import org.example.realworldspringboot.model.entity.User;
import org.example.realworldspringboot.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleFavoritedService {

    private final ArticleRepo articleRepo;
    private final UserRepo userRepo;
    private final ArticleFavoritedRepo articleFavoritedRepo;
    private final ArticleTagRepo articleTagRepo;
    private final UserFollowRepo userFollowRepo;

    @Transactional
    public ArticleResponse favoriteArticle(String slug, String currentUserUsername) {
        User currentUser = userRepo.findUserByUsername(currentUserUsername).orElseThrow(UserNotFoundException::new);
        Article article = articleRepo.findArticleBySlug(slug).orElseThrow(ArticleNotFoundException::new);

        ArticleFavorited articleFavorited = new ArticleFavorited(currentUser, article);

        if(articleFavoritedRepo.existsArticleFavoritedByArticleAndUser(article, currentUser)) {
            throw new AlreadyFavoritedException();
        }
        articleFavoritedRepo.save(articleFavorited);

        return buildArticleResponse(currentUser, article);
    }

    @Transactional
    public void unfavoriteArticle(String slug, String currentUserUsername) {
        User currentUser = userRepo.findUserByUsername(currentUserUsername).orElseThrow(UserNotFoundException::new);
        Article article = articleRepo.findArticleBySlug(slug).orElseThrow(ArticleNotFoundException::new);

        ArticleFavorited articleFavoritedToDelete = articleFavoritedRepo.findArticleFavoritedByArticleAndUser(article, currentUser).orElseThrow(ArticleFavoritedNotFoundException::new);
        articleFavoritedRepo.delete(articleFavoritedToDelete);
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
