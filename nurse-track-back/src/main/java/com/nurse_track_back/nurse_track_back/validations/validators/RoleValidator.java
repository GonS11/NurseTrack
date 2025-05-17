package com.nurse_track_back.nurse_track_back.validations.validators;

import com.nurse_track_back.nurse_track_back.domain.enums.Role;
import com.nurse_track_back.nurse_track_back.repositories.UserRepository;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidUserRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class RoleValidator implements ConstraintValidator<ValidUserRole, Long>
{
    @Autowired
    private UserRepository userRepository;

    private List<Role> allowedRoles;

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
