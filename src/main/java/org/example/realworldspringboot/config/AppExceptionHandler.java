package org.example.realworldspringboot.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.example.realworldspringboot.config.exceptions.UserAlreadyExistsException;
import org.example.realworldspringboot.config.exceptions.UserNotFoundException;
import org.example.realworldspringboot.config.exceptions.UsernameOrEmailIsAlreadyTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundExceptionHandler(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userAlreadyExistsExceptionHandler(UserAlreadyExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UsernameOrEmailIsAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String usernameOrEmailIsAlreadyTakenExceptionHandler(UsernameOrEmailIsAlreadyTakenException ex) {
        return ex.getMessage();
    }
}
