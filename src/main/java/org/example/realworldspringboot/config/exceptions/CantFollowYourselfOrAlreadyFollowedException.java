package org.example.realworldspringboot.config.exceptions;

public class CantFollowYourselfOrAlreadyFollowedException extends RuntimeException{
    public CantFollowYourselfOrAlreadyFollowedException() {
        super("You cant follow yourself or you have already followed the user!");
    }
}
