package ru.codecrafters.AuthorizationTatarBuAPI.exceptions;

import ru.codecrafters.AuthorizationTatarBuAPI.controllers.ClassroomsController;

public class ClassroomException extends RuntimeException{
    public ClassroomException(String message){
        super(message);
    }
}
