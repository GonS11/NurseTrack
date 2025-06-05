package com.nurse_track_back.nurse_track_back.repositories;

import com.nurse_track_back.nurse_track_back.domain.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByName(String departmentName);

    List<Department> findByIsActiveTrue();

    List<Department> findByIsActiveFalse();

    @Query("SELECT d FROM Department d WHERE NOT EXISTS (SELECT 1 FROM SupervisorDepartment sd WHERE sd.department = d)")
    List<Department> findSupervisorUnassignedDepartments();

    @Query("SELECT d FROM Department d WHERE NOT EXISTS (SELECT 1 FROM NurseDepartment nd WHERE nd.department = d)")
    List<Department> findNurseUnassignedDepartments();

    Optional<Department> findByName(String name);
}
