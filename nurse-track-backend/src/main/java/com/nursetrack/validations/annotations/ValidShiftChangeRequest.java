package com.nursetrack.validations.annotations;

import com.nursetrack.validations.validators.ShiftChangeRequestValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ShiftChangeRequestValidator.class)
public @interface ValidShiftChangeRequest
{
    String message() default "Invalid shift change request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
