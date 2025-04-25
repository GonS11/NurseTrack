package com.nursetrack.web.dto.response;

import com.nursetrack.domain.enums.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserResponse
{
    private String username;
    private UserRole role;
    private String fullName;
    private String email;
    private String licenseNumber;
    private Boolean isActive;
}