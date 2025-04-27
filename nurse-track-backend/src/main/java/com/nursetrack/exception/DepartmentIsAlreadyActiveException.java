package com.nursetrack.exception;

public class DepartmentIsAlreadyActiveException extends RuntimeException
{
    public DepartmentIsAlreadyActiveException(Long id)
    {
        super("Department with id " + id + " it is already active");
    }
}
