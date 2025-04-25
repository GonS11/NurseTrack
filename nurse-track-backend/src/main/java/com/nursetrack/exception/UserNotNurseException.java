package com.nursetrack.exception;

public class UserNotNurseException extends RuntimeException {

    public UserNotNurseException(Long id)
    {
        super("User with id " + id + "does not belong to a nurse");
    }
}
