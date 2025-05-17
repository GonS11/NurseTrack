package com.nurse_track_back.nurse_track_back.validations.annotations;

import com.nurse_track_back.nurse_track_back.validations.validators.LicenseNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LicenseNumberValidator.class)
@Documented
public @interface ValidLicenseNumber
{
    String message() default "Invalid license number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

