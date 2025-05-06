package com.nursetrack.repository;

import com.nursetrack.domain.enums.Status;
import com.nursetrack.domain.model.ShiftChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftChangeRequestRepository extends JpaRepository<ShiftChangeRequest, Long>
{
    List<ShiftChangeRequest> findByRequestingNurseId(Long nurseId);
    List<ShiftChangeRequest> findByReceivingNurseId(Long nurseId);

    @Query("SELECT scr FROM ShiftChangeRequest scr " +
            "WHERE (scr.offeredShift.department.id = :departmentId OR scr.desiredShift.department.id = :departmentId) " +
            "AND scr.status = :status")
    List<ShiftChangeRequest> findByDepartmentAndStatus(@Param("departmentId") Long departmentId,
                                                       @Param("status") Status status);

    @Query("SELECT scr FROM ShiftChangeRequest scr " +
            "WHERE scr.offeredShift.department.id = :departmentId OR scr.desiredShift.department.id = :departmentId")
    List<ShiftChangeRequest> findByDepartment(@Param("departmentId") Long departmentId);
}