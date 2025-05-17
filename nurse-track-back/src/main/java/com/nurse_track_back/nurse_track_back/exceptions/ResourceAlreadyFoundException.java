package com.nurse_track_back.nurse_track_back.exceptions;

public class ResourceAlreadyFoundException extends BusinessException
{
    // Constructor para casos generales
    public ResourceAlreadyFoundException(String message)
    {
        super(message);
    }

    // Fx factory para simplificar creación
    public static ResourceAlreadyFoundException create(String resourceName, Object identifier)
    {
        String message = String.format("%s already exists with identifier: %s", resourceName, identifier);
        return new ResourceAlreadyFoundException(message);
    }
}