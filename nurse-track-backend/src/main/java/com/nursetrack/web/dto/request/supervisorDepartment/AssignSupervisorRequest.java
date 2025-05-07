package com.nursetrack.web.dto.request.supervisorDepartment;

import com.nursetrack.domain.enums.UserRole;
import com.nursetrack.validations.annotations.ValidDepartmentId;
import com.nursetrack.validations.annotations.ValidSupervisorAssignment;
import com.nursetrack.validations.annotations.ValidUserId;
import com.nursetrack.validations.annotations.ValidUserRole;
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
    @ValidUserRole(allowedRoles = {UserRole.SUPERVISOR}, message = "User must have the role of Supervisor")
    @ValidUserId(message = "Invalid supervisor ID")
    private Long supervisorId;
}
