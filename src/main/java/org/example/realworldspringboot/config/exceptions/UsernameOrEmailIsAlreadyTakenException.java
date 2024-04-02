package org.example.realworldspringboot.config.exceptions;

public class UsernameOrEmailIsAlreadyTakenException extends RuntimeException{
    public UsernameOrEmailIsAlreadyTakenException() {
        super("Username or email is already taken!");
    }
}
