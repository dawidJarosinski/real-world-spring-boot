package org.example.realworldspringboot.config.exceptions;

public class ArticleFavoritedNotFoundException extends RuntimeException{
    public ArticleFavoritedNotFoundException() {
        super("You haven't favorited this article!");
    }
}
