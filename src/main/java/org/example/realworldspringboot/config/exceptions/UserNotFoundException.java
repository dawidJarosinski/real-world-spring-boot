package org.example.realworldspringboot.config.exceptions;



public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found!");
    }
}
