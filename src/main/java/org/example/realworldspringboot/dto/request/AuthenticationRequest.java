package org.example.realworldspringboot.dto.request;



public record AuthenticationRequest(User user) {
    public record User(String email, String password){}
}
