package com.nurse_track_back.nurse_track_back.services;

import com.nurse_track_back.nurse_track_back.domain.models.Department;
import com.nurse_track_back.nurse_track_back.web.dto.request.department.CreateDepartmentRequest;
import com.nurse_track_back.nurse_track_back.web.dto.request.department.UpdateDepartmentRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.DepartmentResponse;
import com.nurse_track_back.nurse_track_back.exceptions.InvalidStatusException;
import com.nurse_track_back.nurse_track_back.exceptions.ResourceNotFoundException;
import com.nurse_track_back.nurse_track_back.web.mappers.DepartmentMapper;
import com.nurse_track_back.nurse_track_back.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional(readOnly = true)
    public Page<DepartmentResponse> getAllDepartments(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Department> departmentsPage = departmentRepository.findAll(pageable);

        // Mapear content del Page manualmente
        return departmentsPage.map(departmentMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllActiveDepartments() {
        return departmentMapper.toDTOList(departmentRepository.findByIsActiveTrue());
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllInactiveDepartments() {
        return departmentMapper.toDTOList(departmentRepository.findByIsActiveFalse());
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("Department", id));

        return departmentMapper.toDTO(department);
    }

    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        Department department = departmentMapper.toEntity(request);
        department.setIsActive(true);

        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDTO(savedDepartment);
    }

    public DepartmentResponse updateDepartment(Long id, UpdateDepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("Department", id));

        departmentMapper.updateModel(request, department);

        Department updateDepartment = departmentRepository.save(department);

        return departmentMapper.toDTO(updateDepartment);
    }

    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("Department", id));

        departmentRepository.delete(department);
    }

    public void desactivateDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("Department", id));

        if (!department.getIsActive()) {
            throw InvalidStatusException.create("inactive", department.getId());
        }

        department.setIsActive(false);
        departmentRepository.save(department);
    }

    public void activateDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.create("Department", id));

        if (department.getIsActive()) {
            throw InvalidStatusException.create("active", department.getId());
        }

        department.setIsActive(true);
        departmentRepository.save(department);
    }

}
