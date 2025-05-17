package com.nurse_track_back.nurse_track_back.web.dto.request.nurseDepartment;

import com.nurse_track_back.nurse_track_back.domain.enums.Role;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidDepartmentId;
import com.nurse_track_back.nurse_track_back.validations.annotations.ValidNurseAssignment;
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
@ValidNurseAssignment
public class AssignNurseRequest
{
    @NotNull(message = "Department ID is required")
    @ValidDepartmentId(message = "Invalid department ID")
    private Long departmentId;

    @NotNull(message = "Nurse ID is required")
    @ValidUserRole(allowedRoles = {Role.NURSE}, message = "User must have the role of Nurse")
    @ValidUserId(message = "Invalid nurse ID")
    private Long nurseId;
}
