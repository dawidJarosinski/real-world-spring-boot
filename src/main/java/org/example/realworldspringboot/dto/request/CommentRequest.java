package org.example.realworldspringboot.dto.request;

public record CommentRequest(Comment comment) {
    public record Comment(String body) {}
}
