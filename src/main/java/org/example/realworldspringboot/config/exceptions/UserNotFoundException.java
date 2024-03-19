package org.example.realworldspringboot.config.exceptions;


import org.springframework.data.crossstore.ChangeSetPersister;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not found!");
    }
}
