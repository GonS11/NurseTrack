package com.nursetrack.validations.annotations;

import com.nursetrack.validations.validators.UserCreationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserCreationValidator.class)
public @interface ValidUserCreation
{
    String message() default "Datos de usuario inválidos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}