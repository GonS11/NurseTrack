package com.nursetrack.validations.annotations;

import com.nursetrack.validations.validators.DepartmentNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DepartmentNameValidator.class)
@Documented
public @interface ValidDepartmentName
{
    String message() default "Invalid department name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
