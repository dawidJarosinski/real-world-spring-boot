package org.example.realworldspringboot.rest;


import lombok.RequiredArgsConstructor;
import org.example.realworldspringboot.dto.response.ArticleResponse;
import org.example.realworldspringboot.service.ArticleFavoritedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArticleFavoritedController {

    private final ArticleFavoritedService articleFavoriteService;

    @PostMapping("/articles/{slug}/favorite")
    public ResponseEntity<ArticleResponse> favoriteArticle(@PathVariable String slug, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articleFavoriteService.favoriteArticle(slug, principal.getName()));
    }

    @DeleteMapping("/articles/{slug}/favorite")
    public void unfavoriteArticle(@PathVariable String slug, Principal principal) {
        articleFavoriteService.unfavoriteArticle(slug, principal.getName());
    }
}
