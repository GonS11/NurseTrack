package com.nursetrack.exception;

public class UserIsAlreadyActiveException extends RuntimeException
{
    public UserIsAlreadyActiveException(Long id)
    {
        super("User with id " + id + " it is already active");
    }
}
