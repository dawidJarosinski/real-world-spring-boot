package org.example.realworldspringboot.dto.response;

import lombok.Builder;

@Builder
public record UserResponse(User user) {
    @Builder
    public record User(String email, String token, String username, String bio, String image) {}
}
