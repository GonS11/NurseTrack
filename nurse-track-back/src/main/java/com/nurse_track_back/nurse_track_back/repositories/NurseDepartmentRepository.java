package com.nurse_track_back.nurse_track_back.repositories;

import com.nurse_track_back.nurse_track_back.domain.models.NurseDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface NurseDepartmentRepository extends JpaRepository<NurseDepartment, Long>
{
    boolean existsByNurseIdAndDepartmentId(Long nurseId, Long departmentId);

    List<NurseDepartment> findAllByDepartmentId(Long departmentId);
    List<NurseDepartment> findAllByNurseId(Long nurseId);

    void deleteByNurseIdAndDepartmentId(Long nurseId, Long departmentId);
}
