package com.nursetrack.exception;

public class ShiftTemplateAlreadyExistsException extends RuntimeException
{
    public ShiftTemplateAlreadyExistsException(String message)
    {
        super(message);
    }
}
