package org.example.realworldspringboot.dto.response;

import lombok.Builder;
import java.util.List;


@Builder
public record TagsResponse(List<String> tags) {
}
