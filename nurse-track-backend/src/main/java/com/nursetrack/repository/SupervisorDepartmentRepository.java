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

    // Verificar si existe una asignación para un departamento
    Boolean existsByDepartmentId(Long departmentId);

    // Eliminar asignación por departamento
    void deleteByDepartmentId(Long departmentId);

    // Verificar si un usuario es supervisor de algún departamento
    Boolean existsBySupervisorId(Long supervisorId);

    Boolean existsBySupervisorIdAndDepartmentId(Integer supervisorId, Integer departmentId);
}
