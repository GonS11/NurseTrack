package com.nurse_track_back.nurse_track_back.exception;

public class DepartmentNotFoundException extends RuntimeException
{
    public DepartmentNotFoundException(Long id)
    {
        super("Department with id " + id + " not found");
    }
}
