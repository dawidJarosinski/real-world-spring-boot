package org.example.realworldspringboot.dto.request;

public record UpdateArticleRequest(Article article) {
    public record Article(String title, String description, String body) {}
}
