package com.nurse_track_back.nurse_track_back.repositories;

import com.nurse_track_back.nurse_track_back.domain.models.Department;
import com.nurse_track_back.nurse_track_back.domain.models.SupervisorDepartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupervisorDepartmentRepository extends JpaRepository<SupervisorDepartment,Long>
{
    Optional<SupervisorDepartment> findByDepartmentId(Long departmentId);

    boolean existsBySupervisorIdAndDepartmentId(Long supervisorId, Long departmentId);

    @Query("SELECT sd.department FROM SupervisorDepartment sd WHERE sd.supervisor.id = :supervisorId")
    List<Department> findDepartmentsBySupervisorId(@Param("supervisorId") Long supervisorId);

    @Query("""
        SELECT CASE WHEN COUNT(sd) > 0 THEN true ELSE false END
        FROM SupervisorDepartment sd
        JOIN NurseDepartment nd ON nd.department.id = sd.department.id
        WHERE nd.nurse.id = :nurseId 
        AND sd.supervisor.id = :supervisorId
    """)
    boolean existsByNurseIdAndSupervisorId(@Param("nurseId") Long nurseId,
                                           @Param("supervisorId") Long supervisorId);
}