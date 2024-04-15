package org.example.realworldspringboot.config.exceptions;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException() {
        super("Comment not found!");
    }
}
