package org.example.realworldspringboot.config.exceptions;

public class ArticleWithThisTitleAlreadyExistsException extends RuntimeException{

    public ArticleWithThisTitleAlreadyExistsException() {
        super("Article with this title already exists!");
    }
}
