package com.nurse_track_back.nurse_track_back.validations.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.nurse_track_back.nurse_track_back.validations.validators.SupervisorAssignmentValidator;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SupervisorAssignmentValidator.class)
public @interface ValidSupervisorAssignment
{
    String message() default "Invalid supervisor assignment data";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
