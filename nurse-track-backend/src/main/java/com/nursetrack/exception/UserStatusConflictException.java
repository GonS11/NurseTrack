package com.nursetrack.exception;

public class UserStatusConflictException extends RuntimeException
{
    public UserStatusConflictException(Long id, boolean active)
    {
        super("User with id " + id + " already have the status of "+ active);
    }
}
