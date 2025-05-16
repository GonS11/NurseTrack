package com.nurse_track_back.nurse_track_back.exception;

public class UserStatusConflictException extends RuntimeException
{
    public UserStatusConflictException(Long id, Boolean activeStatus)
    {
        super("User with id " + id + " already have the status of " + activeStatus);
    }
}
