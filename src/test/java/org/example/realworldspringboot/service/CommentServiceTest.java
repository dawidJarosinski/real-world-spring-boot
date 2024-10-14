package org.example.realworldspringboot.service;

import org.example.realworldspringboot.config.exceptions.CantManageOtherUsersCommentsException;
import org.example.realworldspringboot.config.exceptions.CommentNotFoundException;
import org.example.realworldspringboot.dto.request.CommentRequest;
import org.example.realworldspringboot.dto.response.CommentResponse;
import org.example.realworldspringboot.model.entity.Article;
import org.example.realworldspringboot.model.entity.Comment;
import org.example.realworldspringboot.model.entity.User;
import org.example.realworldspringboot.repo.ArticleRepo;
import org.example.realworldspringboot.repo.CommentRepo;
import org.example.realworldspringboot.repo.UserFollowRepo;
import org.example.realworldspringboot.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepo commentRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private ArticleRepo articleRepo;
    @Mock
    private UserFollowRepo userFollowRepo;

    private CommentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CommentService(commentRepo, userRepo, articleRepo, userFollowRepo);
    }

    @Test
    void CreateComment_InvokeMethod_ShouldCreateComment() {
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        Article article = new Article("title", "description", "body", user);
        given(userRepo.findUserByUsername(anyString())).willReturn(Optional.of(user));
        given(articleRepo.findArticleBySlug(anyString())).willReturn(Optional.of(article));
        CommentRequest commentRequest = new CommentRequest(new CommentRequest.Comment("comment body"));


        CommentResponse commentResponse = underTest.createComment(commentRequest, anyString(), user.getUsername());


        ArgumentCaptor<Comment> commentArgumentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepo).save(commentArgumentCaptor.capture());
        Comment capturedComment = commentArgumentCaptor.getValue();
        assertThat(capturedComment.getAuthor().getUsername()).isEqualTo(commentResponse.comment().author().username());
        assertThat(capturedComment.getArticle()).isEqualTo(article);
        assertThat(capturedComment.getContent()).isEqualTo(commentRequest.comment().body());
    }

    @Test
    void GetCommentsFromArticle_InvokeMethod_ShouldReturnComments() {
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        Article article = new Article("title", "description", "body", user);
        given(userRepo.findUserByUsername(anyString())).willReturn(Optional.of(user));
        given(articleRepo.findArticleBySlug(anyString())).willReturn(Optional.of(article));

        Comment comment = new Comment("content", user, article);
        given(commentRepo.findCommentsByArticle(article)).willReturn(List.of(comment));

        var response = underTest.getCommentsFromArticle("title", user.getUsername());

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).comment().body()).isEqualTo(comment.getContent());
    }

    @Test
    void DeleteCommentFromArticle_InvokeMethod_ShouldDeleteComment() {
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        Article article = new Article("title", "description", "body", user);
        given(userRepo.findUserByUsername(anyString())).willReturn(Optional.of(user));
        given(articleRepo.findArticleBySlug(anyString())).willReturn(Optional.of(article));

        Comment comment = new Comment("content", user, article);
        given(commentRepo.findCommentById(anyInt())).willReturn(Optional.of(comment));

        underTest.deleteCommentFromArticle("slug", 1, user.getUsername());

        verify(commentRepo).delete(comment);
    }

    @Test
    void DeleteCommentFromArticle_CurrentUserIsNotOwner_ShouldThrowException() {
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        User commentOwner = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username2")
                .build();
        Article article = new Article("title", "description", "body", user);
        given(userRepo.findUserByUsername(anyString())).willReturn(Optional.of(user));
        given(articleRepo.findArticleBySlug(anyString())).willReturn(Optional.of(article));

        Comment comment = new Comment("content", commentOwner, article);
        given(commentRepo.findCommentById(anyInt())).willReturn(Optional.of(comment));


        assertThatThrownBy(() -> underTest.deleteCommentFromArticle("slug", 1, user.getUsername()))
                .isInstanceOf(CantManageOtherUsersCommentsException.class);
        verify(commentRepo, never()).delete(any());
    }

    @Test
    void DeleteCommentFromArticle_SlugDoesNotMatchTheArticleFromComment_ShouldDeleteComment() {
        User user = User.builder()
                .email("test@gmail.com")
                .image("image")
                .bio("bio")
                .id(1)
                .password("password")
                .username("username")
                .build();
        Article article1 = new Article("title", "description", "body", user);
        Article article2 = new Article("title2", "description", "body", user);
        given(userRepo.findUserByUsername(anyString())).willReturn(Optional.of(user));
        given(articleRepo.findArticleBySlug(anyString())).willReturn(Optional.of(article1));

        Comment comment = new Comment("content", user, article2);
        given(commentRepo.findCommentById(anyInt())).willReturn(Optional.of(comment));


        assertThatThrownBy(() -> underTest.deleteCommentFromArticle("slug", 1, user.getUsername()))
                .isInstanceOf(CommentNotFoundException.class);
        verify(commentRepo, never()).delete(any());
    }
}