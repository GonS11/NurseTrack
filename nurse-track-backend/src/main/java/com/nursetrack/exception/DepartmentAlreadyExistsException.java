package com.nursetrack.exception;

public class DepartmentAlreadyExistsException extends RuntimeException
{
    public DepartmentAlreadyExistsException(String name)
    {
        super("Department already exists: " + name);
    }
}
