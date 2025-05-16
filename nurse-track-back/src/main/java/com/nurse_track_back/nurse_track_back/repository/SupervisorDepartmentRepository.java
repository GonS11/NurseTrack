package com.nurse_track_back.nurse_track_back.repository;

import com.nurse_track_back.nurse_track_back.domain.model.SupervisorDepartment;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Locale;
import java.util.Optional;

@Repository
public interface SupervisorDepartmentRepository extends JpaRepository<SupervisorDepartment,Long>
{
    Optional<SupervisorDepartment> findByDepartmentId(Long departmentId);
    Optional<SupervisorDepartment> findBySupervisorId(Long supervisorId);
}
