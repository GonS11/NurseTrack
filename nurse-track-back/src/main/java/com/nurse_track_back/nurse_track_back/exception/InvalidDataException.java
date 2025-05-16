package com.nurse_track_back.nurse_track_back.exception;

public class InvalidDataException extends BusinessException {
    public InvalidDataException(String message) {
        super(message);
    }

    // Factory para datos inválidos
    public static InvalidDataException create(String fieldName, String validationRule) {
        String message = String.format("Invalid data for field '%s': %s", fieldName, validationRule);
        return new InvalidDataException(message);
    }
}