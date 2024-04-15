package org.example.realworldspringboot.config.exceptions;

public class CantManageOtherUsersCommentsException extends RuntimeException{

    public CantManageOtherUsersCommentsException() {
        super("Can't manage other users comments!");
    }
}
