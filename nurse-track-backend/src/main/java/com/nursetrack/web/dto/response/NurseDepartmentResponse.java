package com.nursetrack.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NurseDepartmentResponse
{
    private Long id;
    private UserResponse nurse;
    private DepartmentResponse department;
    private LocalDateTime assignedAt;
}