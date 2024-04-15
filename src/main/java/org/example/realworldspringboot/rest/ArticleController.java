package org.example.realworldspringboot.rest;


import lombok.RequiredArgsConstructor;
import org.example.realworldspringboot.dto.request.ArticleRequest;
import org.example.realworldspringboot.dto.request.UpdateArticleRequest;
import org.example.realworldspringboot.dto.response.ArticleResponse;
import org.example.realworldspringboot.model.entity.Article;
import org.example.realworldspringboot.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles(@RequestParam(required = false) String tag,
                                                         @RequestParam(required = false) String author,
                                                         @RequestParam(required = false) String favorited,
                                                                 Principal principal) {
//        Map<String, String> params = new HashMap<>();
//        if(tag != null) {
//            params.put("tag", tag);
//        }
//        if(author != null) {
//            params.put("author", tag);
//        }
//        if(favorited != null) {
//            params.put("favorited", tag);
//        }
        return ResponseEntity.ok(articleService.findAllArticles(principal.getName()));
    }

    @PostMapping("/articles")
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody ArticleRequest request, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articleService.createArticle(request, principal.getName()));
    }

    @PutMapping("/articles/{slug}")
    public ResponseEntity<ArticleResponse> updateArticle(@RequestBody UpdateArticleRequest request,
                                                         @PathVariable String slug,
                                                         Principal principal) {
        return ResponseEntity.ok(articleService.updateArticle(request, slug, principal.getName()));
    }

    @DeleteMapping("/articles/{slug}")
    public void deleteArticle(@PathVariable String slug, Principal principal) {
        articleService.deleteArticle(slug, principal.getName());
    }
}
