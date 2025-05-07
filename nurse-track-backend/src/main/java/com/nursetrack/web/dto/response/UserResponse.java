package com.nursetrack.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private UserRole role;
    private Boolean isActive;
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getFullName() 
    {
        return firstName + " " + lastName;
    }
}
