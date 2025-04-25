package com.nursetrack.web.mappers.support;

import com.nursetrack.domain.model.Department;
import com.nursetrack.domain.model.User;
import com.nursetrack.repository.DepartmentRepository;
import com.nursetrack.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

public class MapperUtils {

    // Versión genérica para cualquier repositorio
    public static <T> T findOrThrow(Supplier<Optional<T>> supplier, String errorMessage) {
        return supplier.get()
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(errorMessage));
    }

    // Versión específica para User
    public static User findUserOrThrow(UserRepository repository, Long id) {
        return findOrThrow(() -> repository.findById(userId),
                           "User not found with ID: " + userId);
    }

    // Versión específica para Department
    public static Department findDepartmentOrThrow(DepartmentRepository repository, Integer deptId) {
        return findOrThrow(() -> repository.findById(deptId),
                           "Department not found with ID: " + deptId);
    }

    public static LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }
}