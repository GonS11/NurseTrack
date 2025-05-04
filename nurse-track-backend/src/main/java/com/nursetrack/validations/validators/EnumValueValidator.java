package com.nursetrack.validations.validators;

import com.nursetrack.validations.annotations.ValidEnumValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValueValidator implements ConstraintValidator<ValidEnumValue, Enum<?>>
{
    private List<String> allowedValues;


    @Override
    public void initialize(ValidEnumValue constraintAnnotation)
    {
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();

        this.allowedValues = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());

        if(constraintAnnotation.allowedValues().length > 0)
        {
            this.allowedValues = Arrays.asList(constraintAnnotation.allowedValues());
        }
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context)
    {
        if(value == null) return true;

        return allowedValues.contains(value.name());
    }
}
