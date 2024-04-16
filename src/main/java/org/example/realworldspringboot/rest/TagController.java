package org.example.realworldspringboot.rest;


import lombok.RequiredArgsConstructor;
import org.example.realworldspringboot.dto.response.TagsResponse;
import org.example.realworldspringboot.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/tags")
    public ResponseEntity<TagsResponse> findAllTags() {
        return ResponseEntity.ok(tagService.findAllTags());
    }
}
