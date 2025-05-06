package com.nursetrack.repository;

import com.nursetrack.domain.model.SupervisorDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupervisorDepartmentRepository extends JpaRepository<SupervisorDepartment, Long>
{
    Optional<SupervisorDepartment> findByDepartmentId(Long departmentId);
    Optional<SupervisorDepartment> findBySupervisorId(Long supervisorId);

    Boolean existsByDepartmentId(Long departmentId);
    Boolean existsBySupervisorId(Long supervisorId);
    Boolean existsBySupervisorIdAndDepartmentId(Long supervisorId, Long departmentId);

    void deleteByDepartmentId(Long departmentId);

    @Query("""
        SELECT CASE WHEN COUNT(sd) > 0 THEN true ELSE false END
        FROM SupervisorDepartment sd
        JOIN NurseDepartment nd ON nd.department.id = sd.department.id
        WHERE nd.nurse.id = :nurseId 
        AND sd.supervisor.id = :supervisorId
    """)
    boolean existsByNurseIdAndSupervisorId(@Param("nurseId") Long nurseId,
                                           @Param("supervisorId") Long supervisorId);}
