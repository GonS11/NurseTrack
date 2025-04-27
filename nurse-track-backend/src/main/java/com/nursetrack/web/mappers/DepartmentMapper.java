package com.nursetrack.web.mappers;

import com.nursetrack.domain.model.Department;
import com.nursetrack.web.dto.request.department.CreateDepartmentRequest;
import com.nursetrack.web.dto.request.department.UpdateDepartmentRequest;
import com.nursetrack.web.dto.response.DepartmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper
{
    // 1. Entity → Response
    DepartmentResponse toDto(Department department); // Todos los demás campos se mapean automáticamente

    // 2. CreateRequest → Entity
    @Mapping(target = "id", ignore = true) // ID es autogenerado
    @Mapping(target = "isActive", constant = "true") // Fuerza valor por defecto
    @Mapping(target = "createdAt", ignore = true) // Se gestiona con @CreationTimestamp
    @Mapping(target = "updatedAt", ignore = true) // Se gestiona con @UpdateTimestamp
    Department toModel(CreateDepartmentRequest request);

    // 3. UpdateRequest → Entity existente
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true) // Se actualizará automáticamente
    void updateModel(UpdateDepartmentRequest request, @MappingTarget Department department);

    // 4. Lista de Entities → Lista de Responses
    List<DepartmentResponse> toDTOList(List<Department> departments);
}