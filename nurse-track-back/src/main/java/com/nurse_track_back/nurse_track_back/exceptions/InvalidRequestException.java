package com.nurse_track_back.nurse_track_back.exceptions;

public class InvalidRequestException extends BusinessException
{
    public InvalidRequestException(String message)
    {
        super(message);
    }

    // Fx para datos inválidos
    public static InvalidRequestException create(Object userId, Object requestId)
    {
        String message = String.format("Invalid request made for user with id '%s'  and request with id %s", userId, requestId);
        return new InvalidRequestException(message);
    }
}