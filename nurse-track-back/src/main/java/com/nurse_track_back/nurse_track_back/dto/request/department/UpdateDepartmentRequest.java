package com.nurse_track_back.nurse_track_back.dto.request.department;

import com.nurse_track_back.nurse_track_back.validation.annotations.ValidDepartmentName;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateDepartmentRequest
{
    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
    @ValidDepartmentName
    private String name;

    @Size(min = 2, max = 100, message = "Location must be between 2 and 100 characters")
    private String location;

    private Boolean isActive;
}
