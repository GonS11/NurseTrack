package com.nurse_track_back.nurse_track_back.repositories;

import com.nurse_track_back.nurse_track_back.domain.models.NurseDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NurseDepartmentRepository extends JpaRepository<NurseDepartment, Long>
{
    Optional<NurseDepartment> findFirstByNurseId(Long nurseId);

    List<NurseDepartment> findAllByDepartmentId(Long departmentId);
    List<NurseDepartment> findAllByNurseId(Long nurseId);

    boolean existsByNurseIdAndDepartmentId(Long nurseId, Long departmentId);

    void deleteByNurseIdAndDepartmentId(Long nurseId, Long departmentId);
}
