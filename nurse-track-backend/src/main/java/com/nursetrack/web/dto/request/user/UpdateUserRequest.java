package com.nursetrack.web.dto.request.user;

import com.nursetrack.validations.annotations.ValidLicenseNumber;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequest
{
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(max = 50) @ValidLicenseNumber
    private String licenseNumber;

    private Boolean isActive;
}