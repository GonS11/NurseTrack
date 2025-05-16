package com.nurse_track_back.nurse_track_back.validation.validators;

import com.nurse_track_back.nurse_track_back.validation.annotations.ValidLicenseNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LicenseNumberValidator implements ConstraintValidator<ValidLicenseNumber, String>
{
    @Override
    public boolean isValid(String licenseNumber, ConstraintValidatorContext context)
    {
        if (licenseNumber == null) return false;

        // Eliminar espacios y convertir a mayúsculas
        String cleaned = licenseNumber.trim().toUpperCase();

        // Validar formato general (letra opcional + guion + dígitos)
        return cleaned.matches("^([A-Z]-)?\\d{4,8}$");
    }
}
