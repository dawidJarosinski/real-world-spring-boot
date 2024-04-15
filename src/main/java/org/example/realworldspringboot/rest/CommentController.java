package org.example.realworldspringboot.rest;


import lombok.RequiredArgsConstructor;
import org.example.realworldspringboot.dto.request.CommentRequest;
import org.example.realworldspringboot.dto.response.CommentResponse;
import org.example.realworldspringboot.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/articles/{slug}/comments")
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest request, @PathVariable String slug, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(request, slug, principal.getName()));
    }

    @GetMapping("/articles/{slug}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsFromArticle(@PathVariable String slug, Principal principal) {
        return ResponseEntity.ok(commentService.getCommentsFromArticle(slug, principal.getName()));
    }

    @DeleteMapping("/articles/{slug}/comments/{id}")
    public void deleteCommentFromArticle(@PathVariable String slug, @PathVariable Integer id, Principal principal) {
        commentService.deleteCommentFromArticle(slug, id, principal.getName());
    }

}
