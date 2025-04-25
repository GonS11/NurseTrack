package com.nursetrack.validations.validators;

import com.nursetrack.domain.enums.UserRole;
import com.nursetrack.repository.UserRepository;
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
            context.buildConstraintViolationWithTemplate("Email already exists")
                    .addPropertyNode("email")
                    .addConstraintViolation();

            valid = false;
        }

        //Username
        if(userRepository.existsByUsername(request.getUsername()))
        {
            context.buildConstraintViolationWithTemplate("Username already exists")
                    .addPropertyNode("username")
                    .addConstraintViolation();

            valid = false;
        }

        //License number para supervisor y nurse
        if((request.getRole() == UserRole.SUPERVISOR || request.getRole() == UserRole.NURSE)
            && (request.getLicenseNumber() == null || request.getLicenseNumber().isBlank()))
        {
            context.buildConstraintViolationWithTemplate("License number required")
                    .addPropertyNode("licenseNumber")
                    .addConstraintViolation();

            valid = false;
        }

        return valid;
    }
}
