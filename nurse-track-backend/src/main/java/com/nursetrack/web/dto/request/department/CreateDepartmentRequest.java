package com.nursetrack.web.dto.request.department;

import com.nursetrack.validations.annotations.ValidDepartmentName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class CreateDepartmentRequest
{
    @NotBlank
    @Size(min = 2, max = 100)
    @ValidDepartmentName
    private String name;

    @NotBlank
    @Size(min = 2, max = 100)
    private String location;
}