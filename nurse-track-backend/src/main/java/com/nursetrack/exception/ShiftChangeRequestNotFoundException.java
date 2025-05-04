package com.nursetrack.exception;

public class ShiftChangeRequestNotFoundException extends RuntimeException
{
    public ShiftChangeRequestNotFoundException(Long id)
    {
        super("Shift change request with id " + id + " not found");
    }
}
