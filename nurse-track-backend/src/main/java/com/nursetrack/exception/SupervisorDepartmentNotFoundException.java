package com.nursetrack.exception;

public class SupervisorDepartmentNotFoundException extends RuntimeException
{
    public SupervisorDepartmentNotFoundException(Long id)
    {
        super("Supervisor not found or assign to department with id: " + id);
    }
}
