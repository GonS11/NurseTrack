package com.nurse_track_back.nurse_track_back.validations.annotations;

import com.nurse_track_back.nurse_track_back.validations.validators.EnumValueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValueValidator.class)
public @interface ValidEnumValue
{
    Class<? extends Enum<?>> enumClass();
    String[] allowedValues() default {};
    String message() default "must be any of {allowedValues}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
