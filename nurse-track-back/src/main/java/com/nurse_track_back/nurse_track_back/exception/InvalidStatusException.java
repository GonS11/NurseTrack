package com.nurse_track_back.nurse_track_back.exception;

public class InvalidStatusException extends BusinessException {
    public InvalidStatusException(String message) {
        super(message);
    }

    // Factory para errores de seguridad
    public static InvalidStatusException create(String status, Object identifier) {
        String message = String.format("Invalid status '%s' for user with identifier %s", status, identifier);
        return new InvalidStatusException(message);
    }
}
