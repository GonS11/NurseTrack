package com.nursetrack.validations.annotations;

import com.nursetrack.validations.validators.VacationRequestValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VacationRequestValidator.class)
public @interface ValidVacationRequest
{
    String message() default "Invalid vacation request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
