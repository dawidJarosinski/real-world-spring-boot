package org.example.realworldspringboot.service;

import lombok.RequiredArgsConstructor;
import org.example.realworldspringboot.dto.response.TagsResponse;
import org.example.realworldspringboot.model.entity.Tag;
import org.example.realworldspringboot.repo.TagRepo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService{
    private final TagRepo tagRepo;

    public TagsResponse findAllTags() {
        return TagsResponse.builder()
                .tags(tagRepo.findAll().stream().map(Tag::getValue).toList())
                .build();
    }
}
