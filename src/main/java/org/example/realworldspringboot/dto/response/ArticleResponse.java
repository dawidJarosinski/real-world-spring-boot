package org.example.realworldspringboot.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ArticleResponse(Article article) {
    @Builder
    public record Article(String slug,
                          String title,
                          String description,
                          String body,
                          List<String> tagList,
                          LocalDateTime createdAt,
                          LocalDateTime updatedAt,
                          Boolean favorited,
                          Integer favoritesCount,
                          ProfileResponse.Profile author) { }
}
