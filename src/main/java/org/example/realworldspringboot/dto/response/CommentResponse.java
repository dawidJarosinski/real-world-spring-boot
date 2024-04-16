package org.example.realworldspringboot.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;


@Builder
public record CommentResponse(Comment comment) {
    @Builder
    public record Comment(Integer id, LocalDateTime createdAt, LocalDateTime updatedAt, String body, ProfileResponse.Profile author) {}
}
