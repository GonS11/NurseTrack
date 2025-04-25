package com.nursetrack.web.dto.request.user;


import com.nursetrack.domain.enums.UserRole;
import com.nursetrack.validations.annotations.ValidLicenseNumber;
import com.nursetrack.validations.annotations.ValidUserCreation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ValidUserCreation
public class CreateUserRequest
{
    @NotBlank @Size(min = 1, max = 50)
    private String firstName;

    @NotBlank @Size(min = 1, max = 50)
    private String lastName;

    @NotBlank @Size(min = 3, max = 50)
    private String username;

    @NotBlank @Email @Size(max = 100)
    private String email;

    @NotBlank @Size(min = 8, max = 255)
    private String password;

    @NotNull
    //@ValidUserRole(allowedRoles = {UserRole.ADMIN, UserRole.SUPERVISOR, UserRole.NURSE})
    //INNECESARIO si quiero permitir todos, con UserRole ya esta
    private UserRole role;

    @Size(max = 50) @ValidLicenseNumber
    private String licenseNumber;
}
