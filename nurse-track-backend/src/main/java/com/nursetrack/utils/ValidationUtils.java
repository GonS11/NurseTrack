package com.nursetrack.utils;

import jakarta.validation.ConstraintValidatorContext;

public class ValidationUtils
{
    public static void addValidationError(ConstraintValidatorContext context, String field, String message)
    {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(field)
                .addConstraintViolation();
    }
}
