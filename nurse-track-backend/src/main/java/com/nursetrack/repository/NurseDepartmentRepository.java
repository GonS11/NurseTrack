package com.nursetrack.repository;

import com.nursetrack.domain.model.NurseDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NurseDepartmentRepository extends JpaRepository<NurseDepartment, Long>
{
    Optional<NurseDepartment> findFirstByDepartmentId(Long departmentId);
    Optional<NurseDepartment> findByNurseIdAndDepartmentId(Long nurseId, Long departmentId);
    Optional<NurseDepartment> findFirstByNurseId(Long nurseId);

    List<NurseDepartment> findAllByNurseId(Long nurseId);
    List<NurseDepartment> findAllByDepartmentId(Long departmentId);

    boolean existsByNurseIdAndDepartmentId(Long nurseId, Long departmentId);

    void deleteByNurseIdAndDepartmentId(Long nurseId, Long departmentId);
}
