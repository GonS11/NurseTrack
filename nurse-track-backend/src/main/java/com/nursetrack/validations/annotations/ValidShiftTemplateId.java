package com.nursetrack.validations.annotations;

import com.nursetrack.validations.validators.ShiftTemplateIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ShiftTemplateIdValidator.class)
public @interface ValidShiftTemplateId
{
    String message() default "Invalid shift template id";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
