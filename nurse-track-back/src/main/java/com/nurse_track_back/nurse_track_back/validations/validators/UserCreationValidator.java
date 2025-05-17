package com.nurse_track_back.nurse_track_back.validations.validators;

import com.nurse_track_back.nurse_track_back.domain.enums.Role;
import com.nurse_track_back.nurse_track_back.web.dto.request.user.CreateUserRequest;
import com.nurse_track_back.nurse_track_back.repositories.UserRepository;
import com.nurse_track_back.nurse_track_back.utils.ValidationUtils;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidUserCreation;
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
            ValidationUtils.addValidationError(context, "email", "Email already exists");

            valid = false;
        }

        //Username
        if(userRepository.existsByUsername(request.getUsername()))
        {
            ValidationUtils.addValidationError(context,"username", "Username already exists");

            valid = false;
        }

        //License number para supervisor y nurse
        if((request.getRole() == Role.SUPERVISOR || request.getRole() == Role.NURSE)
                && (request.getLicenseNumber() == null || request.getLicenseNumber().isBlank()))
        {
            ValidationUtils.addValidationError(context,"licenseNumber", "License number required");

            valid = false;
        }

        return valid;
    }
}
