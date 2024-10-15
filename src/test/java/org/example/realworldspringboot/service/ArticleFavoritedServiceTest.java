package org.example.realworldspringboot.service;

import org.example.realworldspringboot.config.exceptions.AlreadyFavoritedException;
import org.example.realworldspringboot.dto.response.ArticleResponse;
import org.example.realworldspringboot.model.entity.Article;
import org.example.realworldspringboot.model.entity.ArticleFavorited;
import org.example.realworldspringboot.model.entity.User;
import org.example.realworldspringboot.repo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArticleFavoritedServiceTest {


    @Mock
    private ArticleRepo articleRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private ArticleFavoritedRepo articleFavoritedRepo;
    @Mock
    private ArticleTagRepo articleTagRepo;
    @Mock
    private UserFollowRepo userFollowRepo;

    private ArticleFavoritedService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ArticleFavoritedService(articleRepo, userRepo, articleFavoritedRepo, articleTagRepo, userFollowRepo);
    }

    @Test
    void FavoriteArticle_InvokeMethod_ShouldFollowArticle() {
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        Article article = new Article("title", "description", "body", user);
        given(userRepo.findUserByUsername("username")).willReturn(Optional.of(user));
        given(articleRepo.findArticleBySlug("title")).willReturn(Optional.of(article));
        given(articleFavoritedRepo.existsArticleFavoritedByArticleAndUser(article, user)).willReturn(false);


        ArticleResponse articleResponse = underTest.favoriteArticle(article.getSlug(), user.getUsername());


        ArgumentCaptor<ArticleFavorited> articleFavoritedArgumentCaptor = ArgumentCaptor.forClass(ArticleFavorited.class);
        verify(articleFavoritedRepo).save(articleFavoritedArgumentCaptor.capture());
        ArticleFavorited capturedArticleFavorite = articleFavoritedArgumentCaptor.getValue();
        assertThat(capturedArticleFavorite.getArticle().getTitle()).isEqualTo(article.getTitle());
        assertThat(capturedArticleFavorite.getUser().getUsername()).isEqualTo(user.getUsername());
        assertThat(articleResponse.article().title()).isEqualTo(article.getTitle());
        assertThat(articleResponse.article().author().username()).isEqualTo(user.getUsername());
    }

    @Test
    void FavoriteArticle_ArticleIsFollowed_ShouldThrowException() {
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        Article article = new Article("title", "description", "body", user);
        given(userRepo.findUserByUsername("username")).willReturn(Optional.of(user));
        given(articleRepo.findArticleBySlug("title")).willReturn(Optional.of(article));
        given(articleFavoritedRepo.existsArticleFavoritedByArticleAndUser(article, user)).willReturn(true);


        assertThatThrownBy(() -> underTest.favoriteArticle(article.getSlug(), user.getUsername()))
                .isInstanceOf(AlreadyFavoritedException.class);
        verify(articleFavoritedRepo, never()).save(any());
    }

    @Test
    void UnfavoriteArticle_InvokeMethod_ShouldUnfavoriteArticle() {
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        Article article = new Article("title", "description", "body", user);
        given(userRepo.findUserByUsername("username")).willReturn(Optional.of(user));
        given(articleRepo.findArticleBySlug("title")).willReturn(Optional.of(article));
        given(articleFavoritedRepo.findArticleFavoritedByArticleAndUser(article, user)).willReturn(Optional.of(new ArticleFavorited(user, article)));


        underTest.unfavoriteArticle(article.getSlug(), user.getUsername());


        ArgumentCaptor<ArticleFavorited> articleFavoritedArgumentCaptor = ArgumentCaptor.forClass(ArticleFavorited.class);
        verify(articleFavoritedRepo).delete(articleFavoritedArgumentCaptor.capture());
        ArticleFavorited capturedArticleFavorite = articleFavoritedArgumentCaptor.getValue();
        assertThat(capturedArticleFavorite.getArticle().getTitle()).isEqualTo(article.getTitle());
        assertThat(capturedArticleFavorite.getUser().getUsername()).isEqualTo(user.getUsername());
    }
}