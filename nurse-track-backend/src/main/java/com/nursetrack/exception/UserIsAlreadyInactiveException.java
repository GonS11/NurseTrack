package com.nursetrack.exception;

public class UserIsAlreadyInactiveException extends RuntimeException
{
    public UserIsAlreadyInactiveException(Long id) {
        super("User with id " + id + " it is already inactive");
    }
}
