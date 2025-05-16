package com.nurse_track_back.nurse_track_back.repository;

import com.nurse_track_back.nurse_track_back.domain.model.ShiftTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftTemplateRepository extends JpaRepository<ShiftTemplate, Long>
{
}
