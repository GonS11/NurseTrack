package com.nurse_track_back.nurse_track_back.validation.annotations;

import com.nurse_track_back.nurse_track_back.validation.validators.DepartmentNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DepartmentNameValidator.class)
public @interface ValidDepartmentName
{
    String message() default "Invalid department name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
