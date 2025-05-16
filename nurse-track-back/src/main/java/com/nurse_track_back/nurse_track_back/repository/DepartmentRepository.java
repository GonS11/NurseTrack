package com.nurse_track_back.nurse_track_back.repository;

import com.nurse_track_back.nurse_track_back.domain.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>
{
    boolean existsByName(String departmentName);

    List<Department> findByIsActiveTrue();

    List<Department> findByIsActiveFalse();
}
