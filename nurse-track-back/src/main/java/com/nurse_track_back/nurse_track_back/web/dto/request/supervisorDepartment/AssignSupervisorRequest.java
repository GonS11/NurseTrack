package com.nurse_track_back.nurse_track_back.web.dto.request.supervisorDepartment;

import com.nurse_track_back.nurse_track_back.domain.enums.Role;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidDepartmentId;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidSupervisorAssignment;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidUserId;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidUserRole;
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
