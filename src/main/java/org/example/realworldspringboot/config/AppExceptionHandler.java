package org.example.realworldspringboot.config;

import org.example.realworldspringboot.config.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundExceptionHandler(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userAlreadyExistsExceptionHandler(UserAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UsernameOrEmailIsAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String usernameOrEmailIsAlreadyTakenExceptionHandler(UsernameOrEmailIsAlreadyTakenException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CantFollowYourselfOrAlreadyFollowedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String cantFollowYourselfOrAlreadyFollowedExceptionHandler(CantFollowYourselfOrAlreadyFollowedException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ArticleWithThisTitleAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String articleWithThisTitleAlreadyExistsExceptionHandler(ArticleWithThisTitleAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String articleNotFoundExceptionHandler(ArticleNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CantManageOtherUsersArticlesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String cantManageOtherUsersArticlesExceptionHandler(CantManageOtherUsersArticlesException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String commentNotFoundExceptionHandler(CommentNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(CantManageOtherUsersCommentsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String cantManageOtherUsersCommentsExceptionHandler(CantManageOtherUsersCommentsException ex) {
        return ex.getMessage();
    }
}
