package com.nurse_track_back.nurse_track_back.exception;

public class UserNotFoundException extends RuntimeException
{
    public UserNotFoundException(Long id)
    {
        super("User with id " + id + " not found");
    }
}
