package com.nursetrack.validations.validators;

import com.nursetrack.domain.enums.UserRole;
import com.nursetrack.repository.UserRepository;
import com.nursetrack.utils.ValidationUtils;
import com.nursetrack.validations.annotations.ValidUserCreation;
import com.nursetrack.web.dto.request.user.CreateUserRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UserCreationValidator implements ConstraintValidator<ValidUserCreation, CreateUserRequest>
{
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(CreateUserRequest request, ConstraintValidatorContext context)
    {
        boolean valid = true;

        //Email
        if(userRepository.existsByEmail(request.getEmail()))
        {
            ValidationUtils.addValidationError(context,"email", "Email already exists");

            valid = false;
        }

        //Username
        if(userRepository.existsByUsername(request.getUsername()))
        {
            ValidationUtils.addValidationError(context,"username", "Username already exists");

            valid = false;
        }

        //License number para supervisor y nurse
        if((request.getRole() == UserRole.SUPERVISOR || request.getRole() == UserRole.NURSE)
            && (request.getLicenseNumber() == null || request.getLicenseNumber().isBlank()))
        {
            ValidationUtils.addValidationError(context,"licenseNumber", "License number required");

            valid = false;
        }

        return valid;
    }
}
