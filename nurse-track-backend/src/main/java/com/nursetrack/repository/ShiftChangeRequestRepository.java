package com.nursetrack.repository;

import com.nursetrack.domain.model.ShiftChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftChangeRequestRepository extends JpaRepository<ShiftChangeRequest,Long>
{
    List<ShiftChangeRequest> findAllByReviewerId(Long supervisorId);

    List<ShiftChangeRequest> findAllByRequesterId(Long nurseId);
}
