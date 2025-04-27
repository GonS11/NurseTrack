package com.nursetrack.web.dto.request.supervisorDepartment;

import com.nursetrack.domain.enums.UserRole;
import com.nursetrack.validations.annotations.ValidDepartmentId;
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
public class AssignSupervisorRequest
{
    @NotNull
    @ValidDepartmentId
    private Long departmentId;

    @NotNull
    @ValidUserRole(allowedRoles = {UserRole.SUPERVISOR})
    @ValidUserId
    private Long supervisorUserId;
}