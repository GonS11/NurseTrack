package com.nursetrack.validations.annotations;

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
