package com.nurse_track_back.nurse_track_back.services;

import com.nurse_track_back.nurse_track_back.domain.models.Department;
import com.nurse_track_back.nurse_track_back.domain.models.SupervisorDepartment;
import com.nurse_track_back.nurse_track_back.exceptions.ResourceNotFoundException;
import com.nurse_track_back.nurse_track_back.exceptions.SecurityException;
import com.nurse_track_back.nurse_track_back.repositories.DepartmentRepository;
import com.nurse_track_back.nurse_track_back.repositories.SupervisorDepartmentRepository;
import com.nurse_track_back.nurse_track_back.repositories.UserRepository;
import com.nurse_track_back.nurse_track_back.web.dto.request.supervisorDepartment.AssignSupervisorRequest;
import com.nurse_track_back.nurse_track_back.web.dto.response.DepartmentResponse;
import com.nurse_track_back.nurse_track_back.web.dto.response.SupervisorDepartmentResponse;
import com.nurse_track_back.nurse_track_back.web.mappers.DepartmentMapper;
import com.nurse_track_back.nurse_track_back.web.mappers.SupervisorDepartmentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SupervisorDepartmentService {
    private final SupervisorDepartmentRepository supervisorDepartmentRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final SupervisorDepartmentMapper supervisorDepartmentMapper;
    private final DepartmentMapper departmentMapper;

    @Transactional(readOnly = true)
    public Page<SupervisorDepartmentResponse> getAllAssignments(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<SupervisorDepartment> assingmentsPage = supervisorDepartmentRepository.findAll(pageable);

        // Mapear content del Page manualmente
        return assingmentsPage.map(supervisorDepartmentMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllUnassignedDepartments() {
        List<Department> unassigned = departmentRepository.findSupervisorUnassignedDepartments();
        return departmentMapper.toDTOList(unassigned);
    }

    @Transactional(readOnly = true)
    public SupervisorDepartmentResponse getByDepartmentId(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw ResourceNotFoundException.create("department", id);
        }

        return supervisorDepartmentRepository.findByDepartmentId(id)
                .map(supervisorDepartmentMapper::toDTO)
                .orElseThrow(() -> ResourceNotFoundException.create("Department", id));
    }

    public SupervisorDepartmentResponse assignSupervisor(AssignSupervisorRequest request) {
        SupervisorDepartment assignment = supervisorDepartmentMapper.toEntity(request, userRepository,
                departmentRepository);

        return supervisorDepartmentMapper.toDTO(supervisorDepartmentRepository.save(assignment));
    }

    public void removeSupervisor(Long departmentId) {
        log.info("Attempting to delete assignment for department ID: {}", departmentId);
        SupervisorDepartment assignment = supervisorDepartmentRepository.findByDepartment_Id(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found for department ID: " + departmentId));

        log.info("Found assignment with ID {} for department ID {}. Deleting...", assignment.getId(), departmentId);
        supervisorDepartmentRepository.delete(assignment);
        log.info("Assignment with ID {} for department ID {} successfully deleted.", assignment.getId(), departmentId);

        // --- DIAGNOSTIC CODE START ---
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            log.info("DIAGNOSTIC: Transaction is still active after delete operation.");
            // You can even try to force a flush here, though delete should trigger it
            // JpaTransactionManager will auto-flush before commit
            // entityManager.flush(); // If you have EntityManager injected
        } else {
            log.warn("DIAGNOSTIC: No active transaction after delete operation. This is unexpected for a @Transactional method.");
        }
        // --- DIAGNOSTIC CODE END ---
    }


    public void validateSupervisorAccess(Long supervisorId, Long departmentId) {
        boolean hasAccess = supervisorDepartmentRepository.existsBySupervisor_IdAndDepartment_Id(supervisorId,
                                                                                                 departmentId);

        if (!hasAccess) {
            throw SecurityException.create("validateSupervisorAccess",
                    "Supervisor with ID " + supervisorId + " does not have access to department ID: " + departmentId);

        }
    }
}