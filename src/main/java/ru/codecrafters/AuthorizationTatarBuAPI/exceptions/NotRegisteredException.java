package ru.codecrafters.AuthorizationTatarBuAPI.exceptions;

public class NotRegisteredException extends RuntimeException{

    public NotRegisteredException(String message){
        super(message);
    }
}
