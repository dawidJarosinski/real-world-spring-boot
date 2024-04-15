package org.example.realworldspringboot.config.exceptions;

public class ArticleNotFoundException extends RuntimeException{
    public ArticleNotFoundException() {
        super("Article not found!");
    }
}
