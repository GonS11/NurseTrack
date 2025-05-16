package com.nurse_track_back.nurse_track_back.mapper;

import com.nurse_track_back.nurse_track_back.domain.model.Department;
import com.nurse_track_back.nurse_track_back.dto.request.department.CreateDepartmentRequest;
import com.nurse_track_back.nurse_track_back.dto.request.department.UpdateDepartmentRequest;
import com.nurse_track_back.nurse_track_back.dto.response.DepartmentResponse;
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
    @Mapping(target = "supervisor", ignore = true)
    @Mapping(target = "nurses", ignore = true)
    Department toEntity(CreateDepartmentRequest request);

    //UpdateRequest -> Entity existente
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "shifts", ignore = true)
    @Mapping(target = "supervisor", ignore = true)
    @Mapping(target = "nurses", ignore = true)
    void updateModel(UpdateDepartmentRequest request, @MappingTarget Department department);

    // Lista Entities -> Lista Responses
    List<DepartmentResponse> toDTOList(List<Department> departments);

}
