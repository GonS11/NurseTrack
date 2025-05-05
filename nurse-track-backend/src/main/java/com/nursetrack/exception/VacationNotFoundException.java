package com.nursetrack.exception;

public class VacationNotFoundException extends RuntimeException
{
    public VacationNotFoundException(Long id)
    {
        super("Vacation request with id " + id + " not found");
    }
}
