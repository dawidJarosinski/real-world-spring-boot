package org.example.realworldspringboot.config.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("User with this username or email already exists!");
    }
}
