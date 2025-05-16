package com.nurse_track_back.nurse_track_back.validation.annotations;

import com.nurse_track_back.nurse_track_back.validation.validators.UserIdValidator;
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
