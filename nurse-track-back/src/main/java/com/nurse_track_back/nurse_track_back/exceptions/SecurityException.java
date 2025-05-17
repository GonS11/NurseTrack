package com.nurse_track_back.nurse_track_back.exceptions;

public class SecurityException extends BusinessException
{
    public SecurityException(String message)
    {
        super(message);
    }

    // Fx para errores de seguridad
    public static SecurityException create(String operation, String reason)
    {
        String message = String.format("Security violation in '%s': %s", operation, reason);
        return new SecurityException(message);
    }
}