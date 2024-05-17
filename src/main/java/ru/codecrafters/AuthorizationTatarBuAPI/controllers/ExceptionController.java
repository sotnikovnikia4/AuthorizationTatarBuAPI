package ru.codecrafters.AuthorizationTatarBuAPI.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.codecrafters.AuthorizationTatarBuAPI.exceptions.NotRegisteredException;
import ru.codecrafters.AuthorizationTatarBuAPI.exceptions.UserDataNotChangedException;
import ru.codecrafters.AuthorizationTatarBuAPI.util.ExceptionMessage;

import java.nio.file.AccessDeniedException;
import java.util.Date;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(NotRegisteredException.class)
    public ResponseEntity<ExceptionMessage> handleException(NotRegisteredException e){
        ExceptionMessage message = new ExceptionMessage(e.getMessage(), new Date());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserDataNotChangedException.class)
    public ResponseEntity<ExceptionMessage> handleException(UserDataNotChangedException e){
        ExceptionMessage message = new ExceptionMessage(e.getMessage(), new Date());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleException(UsernameNotFoundException e){
        ExceptionMessage message = new ExceptionMessage(e.getMessage(), new Date());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionMessage> handleException(BadCredentialsException e){
        ExceptionMessage message = new ExceptionMessage(e.getMessage(), new Date());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ExceptionMessage> handleException(JWTVerificationException e){
        ExceptionMessage response = new ExceptionMessage(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionMessage> handleException(AccessDeniedException e){
        ExceptionMessage response = new ExceptionMessage(
                e.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
