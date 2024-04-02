package org.example.realworldspringboot.dto.response;

import lombok.Builder;

@Builder
public record ProfileResponse(Profile profile) {
    @Builder
    public record Profile(String username, String bio, String image, Boolean following) {}
}
