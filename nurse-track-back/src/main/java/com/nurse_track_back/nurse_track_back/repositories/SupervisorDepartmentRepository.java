package com.nurse_track_back.nurse_track_back.repositories;

import com.nurse_track_back.nurse_track_back.domain.models.SupervisorDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupervisorDepartmentRepository extends JpaRepository<SupervisorDepartment,Long>
{
    Optional<SupervisorDepartment> findByDepartmentId(Long departmentId);
    //List<SupervisorDepartment> findAllBySupervisorId(Long supervisorId);

    boolean existsBySupervisorIdAndDepartmentId(Long supervisorId, Long departmentId);

    boolean existsByDepartmentId(Long departmentId); // Para validar asignación única
    List<SupervisorDepartment> findBySupervisorId(Long supervisorId); // Todos los departamentos de un supervisor

}
