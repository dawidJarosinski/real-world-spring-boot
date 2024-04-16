package org.example.realworldspringboot.dto.request;

import java.util.List;

public record ArticleRequest(Article article) {
    public record Article(String title, String description, String body, List<String>tagList) {}
}
