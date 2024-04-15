package org.example.realworldspringboot.config.exceptions;

public class CantManageOtherUsersArticlesException extends RuntimeException{
    public CantManageOtherUsersArticlesException() {
        super("Cant manage other user's articles!");
    }
}
