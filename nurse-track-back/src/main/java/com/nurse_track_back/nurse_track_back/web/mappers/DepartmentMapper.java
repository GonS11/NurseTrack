package com.nurse_track_back.nurse_track_back.web.mappers;

import com.nurse_track_back.nurse_track_back.domain.models.Department;
import com.nurse_track_back.nurse_track_back.web.dto.request.department.CreateDepartmentRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.department.UpdateDepartmentRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.DepartmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper
{
    // Entity -> Response
    DepartmentResponse toDTO(Department department);

    // CreateRequest -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "shifts", ignore = true)
    @Mapping(target = "supervisorAssignment", ignore = true)
    @Mapping(target = "nursesAssignments", ignore = true)
    Department toEntity(CreateDepartmentRequest request);

    //UpdateRequest -> Entity existente
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "shifts", ignore = true)
    @Mapping(target = "supervisorAssignment", ignore = true)
    @Mapping(target = "nursesAssignments", ignore = true)
    void updateModel(UpdateDepartmentRequest request, @MappingTarget Department department);

    // Lista Entities -> Lista Responses
    List<DepartmentResponse> toDTOList(List<Department> departments);

}