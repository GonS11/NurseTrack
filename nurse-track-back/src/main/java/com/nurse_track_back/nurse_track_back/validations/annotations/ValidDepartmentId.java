package com.nurse_track_back.nurse_track_back.validations.annotations;

import com.nurse_track_back.nurse_track_back.validations.validators.DepartmentIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DepartmentIdValidator.class)
@Documented
public @interface ValidDepartmentId
{
    String message() default "Invalid department name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
