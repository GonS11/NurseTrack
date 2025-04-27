package com.nursetrack.validations.annotations;

import com.nursetrack.validations.validators.UserIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserIdValidator.class)
public @interface ValidUserId
{
    String message() default "Invalid user id";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
