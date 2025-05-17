package com.nurse_track_back.nurse_track_back.exceptions;

public class InvalidDataException extends BusinessException
{
    public InvalidDataException(String message)
    {
        super(message);
    }

    // Fx para datos inválidos
    public static InvalidDataException create(String fieldName, String validationRule)
    {
        String message = String.format("Invalid data for field '%s': %s", fieldName, validationRule);
        return new InvalidDataException(message);
    }
}