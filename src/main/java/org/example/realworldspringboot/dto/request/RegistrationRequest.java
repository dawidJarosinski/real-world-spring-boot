package org.example.realworldspringboot.dto.request;

public record RegistrationRequest(User user) {
    public record User(String username, String email, String password) {}
}
