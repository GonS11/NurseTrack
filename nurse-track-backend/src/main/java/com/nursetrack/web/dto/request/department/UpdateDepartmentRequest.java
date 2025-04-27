package com.nursetrack.web.dto.request.department;

import com.nursetrack.validations.annotations.ValidDepartmentName;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateDepartmentRequest
{
    @Size(min = 2, max = 100)
    @ValidDepartmentName
    private String name;

    @Size(min = 2, max = 100)
    private String location;

    private Boolean isActive;
}