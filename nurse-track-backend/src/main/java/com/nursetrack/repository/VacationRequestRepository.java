package com.nursetrack.repository;

import com.nursetrack.domain.enums.Status;
import com.nursetrack.domain.model.VacationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long> {

    @Query("""
        SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END
        FROM VacationRequest v
        WHERE v.requester.id = :nurseId
        AND v.status = :status
        AND (
            (v.startDate BETWEEN :startDate AND :endDate) OR
            (v.endDate BETWEEN :startDate AND :endDate) OR
            (v.startDate <= :startDate AND v.endDate >= :endDate)
        )
    """)
    boolean existsByRequesterAndStatusAndDateRangeOverlap(@Param("nurseId") Long nurseId,
                                                          @Param("status") Status status,
                                                          @Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate);
}