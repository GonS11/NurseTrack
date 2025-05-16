package com.nurse_track_back.nurse_track_back.exception;

public class ShiftTemplateNotFoundException extends RuntimeException
{
    public ShiftTemplateNotFoundException(Long id)
    {
        super("Shift template with id " + id + " not found");
    }
}
