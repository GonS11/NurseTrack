package com.nurse_track_back.nurse_track_back.repositories;

import com.nurse_track_back.nurse_track_back.domain.models.NurseDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NurseDepartmentRepository extends JpaRepository<NurseDepartment, Long>
{
    Optional<NurseDepartment> findFirstByNurse_Id(Long nurseId);

    List<NurseDepartment> findAllByDepartment_Id(Long departmentId);
    List<NurseDepartment> findAllByNurse_Id(Long nurseId);

    boolean existsByNurse_IdAndDepartment_Id(Long nurseId, Long departmentId);

    void deleteByNurse_IdAndDepartment_Id(Long nurseId, Long departmentId);
}
