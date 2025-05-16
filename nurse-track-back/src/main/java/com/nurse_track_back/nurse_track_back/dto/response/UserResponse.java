package com.nurse_track_back.nurse_track_back.dto.response;

import com.nurse_track_back.nurse_track_back.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse
{
    private Long id;
    private String firstname;
    private String lastname;
    private String username;
    private Role role;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
