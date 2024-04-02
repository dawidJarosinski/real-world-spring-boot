package org.example.realworldspringboot.dto.request;

public record UserRequest(User user) {
    public record User(String email, String password, String username, String image, String bio) {}
}
