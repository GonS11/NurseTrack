package com.nursetrack.service;

import com.nursetrack.domain.model.SupervisorDepartment;
import com.nursetrack.exception.DepartmentNotFoundException;
import com.nursetrack.exception.SupervisorDepartmentNotFoundException;
import com.nursetrack.repository.DepartmentRepository;
import com.nursetrack.repository.SupervisorDepartmentRepository;
import com.nursetrack.repository.UserRepository;
import com.nursetrack.web.dto.request.supervisorDepartment.AssignSupervisorRequest;
import com.nursetrack.web.dto.response.SupervisorDepartmentResponse;
import com.nursetrack.web.mappers.SupervisorDepartmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

@Service
@Transactional
@RequiredArgsConstructor
public class SupervisorDepartmentService
{
    private final SupervisorDepartmentRepository supervisorDepartmentRepository;
    private final DepartmentRepository departmentRepository;
    private final SupervisorDepartmentMapper supervisorDepartmentMapper;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public SupervisorDepartmentResponse getByDepartmentId(Long departmentId)
    {
        if(!departmentRepository.existsById(departmentId)) throw new DepartmentNotFoundException(departmentId);

        return supervisorDepartmentRepository.findByDepartmentId(departmentId)
                .map(supervisorDepartmentMapper::toDto)
                .orElseThrow(() -> new SupervisorDepartmentNotFoundException(departmentId));
    }


    public SupervisorDepartmentResponse assignSupervisor(AssignSupervisorRequest request)
    {
        SupervisorDepartment assignment = supervisorDepartmentMapper.toModel(request);

        return supervisorDepartmentMapper.toDto(supervisorDepartmentRepository.save(assignment));
    }


    public void removeSupervisor(Long departmentId)
    {
        if(!departmentRepository.existsById(departmentId)) throw new DepartmentNotFoundException(departmentId);

        if (!supervisorDepartmentRepository.existsByDepartmentId(departmentId)) throw new SupervisorDepartmentNotFoundException(departmentId);

        supervisorDepartmentRepository.deleteByDepartmentId(departmentId);
    }

    public void validateSupervisorAccess(Integer supervisorId, Integer departmentId)
            throws AccessDeniedException
    {
        if(!supervisorDepartmentRepository.existsBySupervisorIdAndDepartmentId(supervisorId, departmentId))
        {
            throw new AccessDeniedException("Supervisor has not access to this department");
        }
    }
}