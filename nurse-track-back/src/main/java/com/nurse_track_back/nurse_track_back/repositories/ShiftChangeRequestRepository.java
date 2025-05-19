package com.nurse_track_back.nurse_track_back.repositories;

import com.nurse_track_back.nurse_track_back.domain.enums.RequestStatus;
import com.nurse_track_back.nurse_track_back.domain.models.ShiftChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftChangeRequestRepository extends JpaRepository<ShiftChangeRequest, Long> {
        List<ShiftChangeRequest> findByRequestingNurseId(Long nurseId);

        List<ShiftChangeRequest> findByReceivingNurseId(Long nurseId);

        @Query("SELECT scr FROM ShiftChangeRequest scr " +
                        "WHERE (scr.offeredShift.department.id = :departmentId OR scr.desiredShift.department.id = :departmentId) "
                        +
                        "AND scr.status = :status")
        List<ShiftChangeRequest> findByDepartmentAndStatus(@Param("departmentId") Long departmentId,
                        @Param("status") RequestStatus status);

        @Query("SELECT scr FROM ShiftChangeRequest scr " +
                        "WHERE scr.offeredShift.department.id = :departmentId OR scr.desiredShift.department.id = :departmentId")
        List<ShiftChangeRequest> findByDepartment(@Param("departmentId") Long departmentId);
}
