package com.nursetrack.web.dto.response;

import com.nursetrack.domain.enums.UserRole;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse
{
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private UserRole role;
    private String licenseNumber;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //FirstName + LastName
    private String fullName;

    //De UserRole
    private String roleDisplayName;
    private String roleDescription;

    //Calculado
    private Integer totalAssignedShifts;
    private Integer unreadNotifications;
}