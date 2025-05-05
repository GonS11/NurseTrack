package com.nursetrack.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nursetrack.domain.enums.UserRole;
import lombok.*;

import java.time.LocalDateTime;

// Para datos públicos del usuario (listados, perfiles)
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
    private UserRole role;
    private Boolean isActive;
    private LocalDateTime createdAt;

    // Elimina @Mapping del mapper para fullName y déjalo solo en el getter
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getFullName()
    {
        return firstName + " " + lastName;
    }
}