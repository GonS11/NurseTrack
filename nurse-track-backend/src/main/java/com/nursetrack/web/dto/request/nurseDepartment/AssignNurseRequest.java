package com.nursetrack.web.dto.request.nurseDepartment;

import com.nursetrack.domain.enums.UserRole;
import com.nursetrack.validations.annotations.ValidDepartmentId;
import com.nursetrack.validations.annotations.ValidNurseAssignment;
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
@ValidNurseAssignment
public class AssignNurseRequest
{
    @NotNull
    @ValidDepartmentId
    private Long departmentId;

    @NotNull
    @ValidUserRole(allowedRoles = {UserRole.NURSE})
    @ValidUserId
    private Long nurseId;
}