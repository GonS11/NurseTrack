package com.nursetrack.validations.validators;

import com.nursetrack.domain.enums.UserRole;
import com.nursetrack.repository.UserRepository;
import com.nursetrack.validations.annotations.ValidUserRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class UserRoleValidator implements ConstraintValidator<ValidUserRole, Long>
{
    @Autowired
    private UserRepository userRepository;

    private List<UserRole> allowedRoles;

    @Override
    public void initialize(ValidUserRole constraintAnnotation)
    {
        this.allowedRoles = Arrays.asList(constraintAnnotation.allowedRoles());
    }

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext context)
    {
        if(userId == null) return true; //Se encarga el @NotNull

        return userRepository.findById(userId)
                .map(user -> allowedRoles.contains(user.getRole()))
                .orElse(false);
    }
}
