package com.nursetrack.exception;

public class ShiftNotFoundException extends RuntimeException
{
    public ShiftNotFoundException(Long id)
    {
        super("Shift with id " + id + " not found");
    }
}
