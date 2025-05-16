package com.nurse_track_back.nurse_track_back.exception;

public class ResourceNotFoundException extends BusinessException {
    // Constructor para casos generales
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Método factory para simplificar creación
    public static ResourceNotFoundException create(String resourceName, Object identifier) {
        String message = String.format("%s not found with identifier: %s", resourceName, identifier);
        return new ResourceNotFoundException(message);
    }
}