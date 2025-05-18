package com.nurse_track_back.nurse_track_back.validations.annotations;

import com.nurse_track_back.nurse_track_back.validations.validators.ShiftIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ShiftIdValidator.class)
public @interface ValidShiftId
{
    String message() default "Invalid shift id";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
