package org.example.realworldspringboot.service;

import org.example.realworldspringboot.dto.response.TagsResponse;
import org.example.realworldspringboot.model.entity.Tag;
import org.example.realworldspringboot.repo.TagRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.AssertionsForClassTypes.*;


@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepo tagRepo;
    private TagService underTest;

    @BeforeEach
    void setUp() {
        underTest = new TagService(tagRepo);
    }

    @Test
    void findAllTags() {
        List<String> values = List.of("tag1", "tag2", "tag3");
        Tag tag1 = new Tag(values.get(0));
        Tag tag2 = new Tag(values.get(1));
        Tag tag3 = new Tag(values.get(2));
        given(tagRepo.findAll()).willReturn(List.of(tag1, tag2, tag3));

        TagsResponse tagsResponse = underTest.findAllTags();

        assertThat(tagsResponse.tags().size()).isEqualTo(3);
        assertThat(tagsResponse.tags()).isEqualTo(values);
    }
}