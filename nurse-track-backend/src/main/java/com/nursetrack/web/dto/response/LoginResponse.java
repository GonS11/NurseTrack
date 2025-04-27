package com.nursetrack.web.dto.response;

import com.nursetrack.domain.enums.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse
{
    private String token;
    private String tokenType;
    private String username;
    private UserRole role;
    private String fullName;
    private String email;
    private String licenseNumber;
}