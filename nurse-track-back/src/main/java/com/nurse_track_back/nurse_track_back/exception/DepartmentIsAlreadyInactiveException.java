package com.nurse_track_back.nurse_track_back.exception;

public class DepartmentIsAlreadyInactiveException extends RuntimeException
{
    public DepartmentIsAlreadyInactiveException(Long id)
    {
        super("Department with id " + id + " it is already inactive");
    }
}
