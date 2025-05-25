package com.nurse_track_back.nurse_track_back.utils;

import com.nurse_track_back.nurse_track_back.domain.models.Department;
import com.nurse_track_back.nurse_track_back.exceptions.InvalidStatusException;
import com.nurse_track_back.nurse_track_back.repositories.DepartmentRepository; // Importar el repositorio
import com.nurse_track_back.nurse_track_back.web.dto.request.department.UpdateDepartmentRequest; // Importar el DTO
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DepartmentValidator {

    private final DepartmentRepository departmentRepository;

    public void validateNameDepartment(Long id, UpdateDepartmentRequest request, Department departmentToUpdate) {
        // Nombre unico
        if (request.getName() != null && !request.getName().isBlank()) {
            // Si el nombre ha cambiado
            if (!departmentToUpdate.getName().equals(request.getName())) {
                Optional<Department> departmentWithSameName = departmentRepository.findByName(request.getName());
                if (departmentWithSameName.isPresent()) {
                    // Si existe otro departamento con este nombre (que no sea el propio)
                    if (!departmentWithSameName.get().getId().equals(id)) {
                        throw new InvalidStatusException("Department with this name already exists.");
                    }
                }
            }
        }
    }
}