package org.example.realworldspringboot.service;

import org.example.realworldspringboot.config.exceptions.ArticleWithThisTitleAlreadyExistsException;
import org.example.realworldspringboot.config.exceptions.CantManageOtherUsersArticlesException;
import org.example.realworldspringboot.config.exceptions.CantManageOtherUsersCommentsException;
import org.example.realworldspringboot.dto.request.ArticleRequest;
import org.example.realworldspringboot.dto.request.UpdateArticleRequest;
import org.example.realworldspringboot.dto.response.ArticleResponse;
import org.example.realworldspringboot.model.entity.*;
import org.example.realworldspringboot.repo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepo articleRepo;
    @Mock
    private TagRepo tagRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private ArticleTagRepo articleTagRepo;
    @Mock
    private ArticleFavoritedRepo articleFavoritedRepo;
    @Mock
    private UserFollowRepo userFollowRepo;

    private ArticleService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ArticleService(articleRepo, tagRepo, userRepo, articleTagRepo, articleFavoritedRepo, userFollowRepo);
    }

    @Test
    void FindAllArticles_InvokeMethodWithTagParams_ShouldReturnArticle() {
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        Article article = new Article("title", "description", "body", user);
        Tag tag = new Tag("tagValue");
        given(userRepo.findUserByUsername(anyString())).willReturn(Optional.of(user));
        given(articleTagRepo.findArticleTagsByArticle(article)).willReturn(List.of(new ArticleTag(article, tag)));
        given(articleFavoritedRepo.findArticleFavoritedByArticle(article)).willReturn(List.of(new ArticleFavorited(user, article)));
        given(articleRepo.findAll()).willReturn(List.of(article));
        Map<String, String> param = new HashMap<>();
        param.put("tag", "tagValue");
        param.put("author", "username");
        param.put("favorited", "username");


        List<ArticleResponse> articleResponses = underTest.findAllArticles(param, "username");


        assertThat(articleResponses.size()).isEqualTo(1);
        assertThat(articleResponses.get(0).article().title()).isEqualTo(article.getTitle());
        assertThat(articleResponses.get(0).article().author().username()).isEqualTo(user.getUsername());
    }

    @Test
    void CreateArticle_InvokeWithCorrectArticleRequest_ShouldSaveAndReturnArticle() {
        ArticleRequest articleRequest = new ArticleRequest(new ArticleRequest.Article(
                "title",
                "description",
                "body",
                List.of("tag1", "tag2")));
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        given(userRepo.findUserByUsername("username")).willReturn(Optional.of(user));
        given(tagRepo.existsTagByValue(anyString())).willReturn(false);
        given(articleRepo.existsArticleByTitle(articleRequest.article().title())).willReturn(false);


        ArticleResponse articleResponse = underTest.createArticle(articleRequest, "username");


        ArgumentCaptor<Article> articleArgumentCaptor = ArgumentCaptor.forClass(Article.class);
        verify(articleRepo).save(articleArgumentCaptor.capture());
        assertThat(articleArgumentCaptor.getValue().getTitle()).isEqualTo(articleRequest.article().title());
        assertThat(articleResponse.article().title()).isEqualTo(articleRequest.article().title());

        verify(tagRepo, times(2)).save(any());
        verify(articleTagRepo, times(2)).save(any());
    }

    @Test
    void CreateArticle_ArticleWithTakenTitle_ShouldThrowException() {
        ArticleRequest articleRequest = new ArticleRequest(new ArticleRequest.Article(
                "title",
                "description",
                "body",
                List.of("tag1", "tag2")));
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        given(articleRepo.existsArticleByTitle(articleRequest.article().title())).willReturn(true);
        given(userRepo.findUserByUsername("username")).willReturn(Optional.of(user));


        assertThatThrownBy(() -> underTest.createArticle(articleRequest, "username"))
                .isInstanceOf(ArticleWithThisTitleAlreadyExistsException.class);
        verify(articleRepo, never()).save(any());
        verify(tagRepo, never()).save(any());
        verify(articleTagRepo, never()).save(any());
    }

    @Test
    void UpdateArticle_CorrectUpdateArticleRequest_ShouldUpdateArticle() {
        UpdateArticleRequest articleRequest = new UpdateArticleRequest(new UpdateArticleRequest.Article(
                "UpdatedTitle",
                "UpdatedDescription",
                "UpdatedBody"));
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        Article article = new Article("title", "description", "body", user);
        given(articleRepo.existsArticleByTitle(articleRequest.article().title())).willReturn(false);
        given(userRepo.findUserByUsername("username")).willReturn(Optional.of(user));
        given(articleRepo.findArticleBySlug("title")).willReturn(Optional.of(article));


        ArticleResponse articleResponse = underTest.updateArticle(articleRequest, "title", "username");


        ArgumentCaptor<Article> articleArgumentCaptor = ArgumentCaptor.forClass(Article.class);
        verify(articleRepo).save(articleArgumentCaptor.capture());
        assertThat(articleArgumentCaptor.getValue().getTitle()).isEqualTo(articleRequest.article().title());
        assertThat(articleResponse.article().title()).isEqualTo(articleRequest.article().title());

        assertThat(articleArgumentCaptor.getValue().getBody()).isEqualTo(articleRequest.article().body());
        assertThat(articleResponse.article().body()).isEqualTo(articleRequest.article().body());

        assertThat(articleArgumentCaptor.getValue().getDescription()).isEqualTo(articleRequest.article().description());
        assertThat(articleResponse.article().description()).isEqualTo(articleRequest.article().description());
    }

    @Test
    void UpdateArticle_TitleIsTaken_ShouldThrowException() {
        UpdateArticleRequest articleRequest = new UpdateArticleRequest(new UpdateArticleRequest.Article(
                "UpdatedTitle",
                "UpdatedDescription",
                "UpdatedBody"));
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        Article article = new Article("title", "description", "body", user);
        given(articleRepo.existsArticleByTitle(articleRequest.article().title())).willReturn(true);
        given(userRepo.findUserByUsername("username")).willReturn(Optional.of(user));
        given(articleRepo.findArticleBySlug("title")).willReturn(Optional.of(article));


        assertThatThrownBy(() -> underTest.updateArticle(articleRequest, "title", "username"))
                .isInstanceOf(ArticleWithThisTitleAlreadyExistsException.class);
        verify(articleRepo, never()).save(any());
    }

    @Test
    void UpdateArticle_ManagingOtherUsersArticle_ShouldThrowException() {
        UpdateArticleRequest articleRequest = new UpdateArticleRequest(new UpdateArticleRequest.Article(
                "UpdatedTitle",
                "UpdatedDescription",
                "UpdatedBody"));
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        Article article = new Article("title", "description", "body", null);
        given(userRepo.findUserByUsername("username")).willReturn(Optional.of(user));
        given(articleRepo.findArticleBySlug("title")).willReturn(Optional.of(article));


        assertThatThrownBy(() -> underTest.updateArticle(articleRequest, "title", "username"))
                .isInstanceOf(CantManageOtherUsersArticlesException.class);
        verify(articleRepo, never()).save(any());
    }

    @Test
    void DeleteArticle_CorrectInvoke_ShouldDeleteArticleWithTag() {
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        Article article = new Article("title", "description", "body", user);
        Tag tag = new Tag("tagValue");
        ArticleTag articleTag = new ArticleTag(article, tag);
        given(userRepo.findUserByUsername("username")).willReturn(Optional.of(user));
        given(articleRepo.findArticleBySlug("title")).willReturn(Optional.of(article));
        given(articleTagRepo.countArticleTagByTag(tag)).willReturn(1);
        given(articleTagRepo.findArticleTagsByArticle(article)).willReturn(List.of(articleTag));


        underTest.deleteArticle(article.getSlug(), user.getUsername());


        verify(tagRepo).delete(tag);
        verify(articleTagRepo).delete(articleTag);
        verify(articleRepo).delete(article);
    }
}