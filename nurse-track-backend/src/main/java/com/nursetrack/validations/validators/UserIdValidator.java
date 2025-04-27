package com.nursetrack.validations.validators;

import com.nursetrack.repository.UserRepository;
import com.nursetrack.validations.annotations.ValidUserId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UserIdValidator implements ConstraintValidator<ValidUserId,Long>
{
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext context)
    {
        if(userId == null) return true;

        return userRepository.existsById(userId);
    }
}
