package com.nursetrack.exception;

public class UserNotSupervisorException extends RuntimeException
{
    public UserNotSupervisorException(Long id)
    {
        super("User with id " + id + "does not belong to a supervisor");
    }
}
