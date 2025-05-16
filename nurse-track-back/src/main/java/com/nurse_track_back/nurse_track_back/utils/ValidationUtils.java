package com.nurse_track_back.nurse_track_back.utils;

import jakarta.validation.ConstraintValidatorContext;

public class ValidationUtils
{
    public static void addValidationError(ConstraintValidatorContext context, String field, String message)
    {
        context.disableDefaultConstraintViolation(); //Asegura que no hay duplicados y asi no ponerlo en el validator de cada uno
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(field)
                .addConstraintViolation();
    }
}
