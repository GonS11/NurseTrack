package com.nursetrack.repository;

import com.nursetrack.domain.model.ShiftTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftTemplateRepository extends JpaRepository<ShiftTemplate, Long>
{
}