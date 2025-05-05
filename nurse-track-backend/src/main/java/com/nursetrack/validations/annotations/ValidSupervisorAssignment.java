package com.nursetrack.validations.annotations;

import com.nursetrack.validations.validators.SupervisorAssignmentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SupervisorAssignmentValidator.class)
public @interface ValidSupervisorAssignment
{
    String message() default "Invalid supervisor assignment data";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
