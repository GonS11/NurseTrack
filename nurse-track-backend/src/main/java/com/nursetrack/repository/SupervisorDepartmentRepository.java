package com.nursetrack.repository;

import com.nursetrack.domain.model.SupervisorDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupervisorDepartmentRepository extends JpaRepository<SupervisorDepartment, Long>
{
    // Encontrar el supervisor asignado a un departamento
    Optional<SupervisorDepartment> findByDepartmentId(Long departmentId);
    Optional<SupervisorDepartment> findBySupervisorId(Long supervisorId);

    // Verificar si existe una asignación para un departamento
    Boolean existsByDepartmentId(Long departmentId);
    Boolean existsBySupervisorId(Long supervisorId);
    Boolean existsBySupervisorIdAndDepartmentId(Long supervisorId, Long departmentId);

    // Eliminar asignación por departamento
    void deleteByDepartmentId(Long departmentId);
}
