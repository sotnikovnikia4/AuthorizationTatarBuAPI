package ru.codecrafters.AuthorizationTatarBuAPI.exceptions;

public class UserDataNotChangedException extends RuntimeException{

    public UserDataNotChangedException(String message){
        super(message);
    }
}
