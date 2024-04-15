package org.example.realworldspringboot.service;


import lombok.RequiredArgsConstructor;
import org.example.realworldspringboot.config.exceptions.ArticleNotFoundException;
import org.example.realworldspringboot.config.exceptions.CantManageOtherUsersCommentsException;
import org.example.realworldspringboot.config.exceptions.CommentNotFoundException;
import org.example.realworldspringboot.dto.request.ArticleRequest;
import org.example.realworldspringboot.dto.request.CommentRequest;
import org.example.realworldspringboot.dto.response.CommentResponse;
import org.example.realworldspringboot.dto.response.ProfileResponse;
import org.example.realworldspringboot.model.entity.Article;
import org.example.realworldspringboot.model.entity.Comment;
import org.example.realworldspringboot.model.entity.User;
import org.example.realworldspringboot.repo.ArticleRepo;
import org.example.realworldspringboot.repo.CommentRepo;
import org.example.realworldspringboot.repo.UserFollowRepo;
import org.example.realworldspringboot.repo.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepo commentRepo;
    private final UserRepo userRepo;
    private final ArticleRepo articleRepo;
    private final UserFollowRepo userFollowRepo;

    @Transactional
    public CommentResponse createComment(CommentRequest request, String slug, String currentUserUsername) {
        User currentUser = userRepo.findUserByUsername(currentUserUsername).get();
        Article currentArticle = articleRepo.findArticleBySlug(slug).orElseThrow(ArticleNotFoundException::new);

        Comment comment = new Comment(request.comment().body(), currentUser, currentArticle);
        commentRepo.save(comment);

        return buildCommentResponse(comment, currentUser);
    }

    public List<CommentResponse> getCommentsFromArticle(String slug, String currentUserUsername) {
        User currentUser = userRepo.findUserByUsername(currentUserUsername).get();
        Article currentArticle = articleRepo.findArticleBySlug(slug).orElseThrow(ArticleNotFoundException::new);

        return commentRepo.findCommentsByArticle(currentArticle).stream().map(comment -> {
            return buildCommentResponse(comment, currentUser);
        }).toList();
    }

    @Transactional
    public void deleteCommentFromArticle(String slug, Integer id, String currentUserUsername) {
        User currentUser = userRepo.findUserByUsername(currentUserUsername).get();
        Comment comment = commentRepo.findCommentById(id).orElseThrow(CommentNotFoundException::new);

        if(!comment.getArticle().equals(articleRepo.findArticleBySlug(slug).orElseThrow(ArticleNotFoundException::new))) {
            throw new CommentNotFoundException();
        }
        if(!comment.getAuthor().equals(currentUser)) {
            throw new CantManageOtherUsersCommentsException();
        }
        commentRepo.delete(comment);
    }

    private CommentResponse buildCommentResponse(Comment comment, User currentUser) {
        return CommentResponse.builder()
                .comment(CommentResponse.Comment.builder()
                        .id(comment.getId())
                        .updatedAt(LocalDateTime.now())
                        .createdAt(LocalDateTime.now())
                        .body(comment.getContent())
                        .author(ProfileResponse.Profile.builder()
                                .username(comment.getAuthor().getUsername())
                                .bio(comment.getAuthor().getBio())
                                .image(comment.getAuthor().getImage())
                                .following(userFollowRepo.existsByFollowerAndFollowed(currentUser, comment.getAuthor()))
                                .build())
                        .build())
                .build();
    }
}
