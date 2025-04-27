package com.nursetrack.exception;

public class ShiftTemplateNotFoundException extends RuntimeException
{
    public ShiftTemplateNotFoundException(Long id)
    {
        super("Shift template not found: " + id);
    }
}
