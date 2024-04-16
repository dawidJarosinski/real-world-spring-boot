package org.example.realworldspringboot.config.exceptions;

public class AlreadyFavoritedException extends RuntimeException{
    public AlreadyFavoritedException() {
        super("You have already favorited this article!");
    }
}
