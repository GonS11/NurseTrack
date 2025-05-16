package com.nurse_track_back.nurse_track_back.dto.request.user;

import com.nurse_track_back.nurse_track_back.validation.annotations.ValidLicenseNumber;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequest
{
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstname;

    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastname;

    @Size(max = 50, message = "License number must not exceed 50 characters")
    @ValidLicenseNumber
    private String licenseNumber;

    //@ValidPassword
    private String password;

    private Boolean isActive;
}
