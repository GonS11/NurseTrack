package com.nursetrack.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleResponse
{
    private Long id;
    private String fullName;  // Calculado en el mapper
    private String username;
}