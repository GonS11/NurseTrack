package com.nurse_track_back.nurse_track_back.dto.request.supervisorDepartment;

import com.nurse_track_back.nurse_track_back.domain.enums.Role;
import com.nurse_track_back.nurse_track_back.validation.annotations.ValidDepartmentId;
import com.nurse_track_back.nurse_track_back.validation.annotations.ValidSupervisorAssignment;
import com.nurse_track_back.nurse_track_back.validation.annotations.ValidUserId;
import com.nurse_track_back.nurse_track_back.validation.annotations.ValidUserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidSupervisorAssignment
public class AssignSupervisorRequest
{
    @NotNull(message = "Department ID is required")
    @ValidDepartmentId(message = "Invalid department ID")
    private Long departmentId;

    @NotNull(message = "Supervisor ID is required")
    @ValidUserRole(allowedRoles = {Role.SUPERVISOR}, message = "User must have the role of Supervisor")
    @ValidUserId(message = "Invalid supervisor ID")
    private Long supervisorId;
}
