package com.nursetrack.web.dto.response;

import com.nursetrack.validations.annotations.ValidSupervisorAssignment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidSupervisorAssignment
public class SupervisorDepartmentResponse
{
    private Long id;
    private UserResponse supervisor;
    private DepartmentResponse department;
    private LocalDateTime assignedAt;
}
